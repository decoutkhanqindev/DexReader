# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## IMPORTANT: Keep This File in Sync

**Whenever any file in the `util/` directory is added, removed, or modified, this CLAUDE.md must be updated in the same session to reflect the change.** This includes:

- New utility object or file added → update overview list
- New function added to `AsyncHandler`, `TimeAgo`, or `NavTransitions` → document its signature
- Existing function signature changed → update the Signatures section
- `TimeAgo` threshold behavior changed → update threshold table/description
- `NavTransitions` animation preset added or removed → update the relevant section

Do not leave this file stale. An outdated CLAUDE.md is worse than no CLAUDE.md.

## util/ Overview

Three `object` singletons. No Android framework dependency in `AsyncHandler` or `TimeAgo` — pure Kotlin/JVM. `NavTransitions` depends on Compose Navigation.

---

## AsyncHandler

Structured exception handling for coroutines. All functions explicitly rethrow `CancellationException` before any catch block — this is load-bearing. Swallowing `CancellationException` creates zombie coroutines that keep running after their scope is cancelled.

### Function selection guide

| Function | Returns | Use when |
|---|---|---|
| `runSuspendResultCatching { }` | `Result<T>` | Use case layer — wraps outcome, no exception mapping needed |
| `runSuspendResultCatching(context) { }` | `Result<T>` | Use case layer with dispatcher switch (e.g. `Dispatchers.IO`) |
| `runSuspendCatching(context, block, catch) { }` | `T` directly | Repository layer — needs exception remapping via `catch` lambda |
| `Flow<T>.toFlowResult()` | `Flow<Result<T>>` | Reactive use cases — wraps each emission, rethrows `CancellationException` |

### Signatures

```kotlin
// Use case: no dispatcher switch
suspend inline fun <T> runSuspendResultCatching(
    crossinline block: suspend () -> T,
): Result<T>

// Use case: with dispatcher switch
suspend inline fun <T> runSuspendResultCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
): Result<T>

// Repository: direct return + exception mapping
suspend inline fun <T> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
    crossinline catch: (Exception) -> T = { throw it },  // default: rethrow
): T

// Reactive use case
fun <T> Flow<T>.toFlowResult(): Flow<Result<T>>
```

`runSuspendCatching` without a `catch` lambda is equivalent to `withContext` — prefer `withContext` directly in that case.

`toFlowResult()` rethrows `CancellationException` from the `.catch` operator — it does **not** absorb it. A `CancellationException` rethrown by `toFlowResult` will propagate through `collect { }` in ViewModels and hit any outer `try/catch`. Always add a `catch (c: CancellationException) { throw c }` guard before `catch (e: Exception)` in VM coroutines.

---

## TimeAgo

Date formatting `object`. Both `SimpleDateFormat` instances use `ThreadLocal` — `SimpleDateFormat` is not thread-safe.

```kotlin
fun String?.parseIso8601ToEpoch(): Long?   // ISO8601 string → epoch millis; returns null on parse failure
fun Long?.toTimeAgo(): String              // epoch millis → human-readable relative string
```

**`toTimeAgo()` thresholds:**

| Diff | Output |
|---|---|
| < 60 s | "X seconds ago" (negative diff → "Unknown time") |
| < 1 h | "X minutes ago" |
| < 24 h | "X hours ago" |
| < 7 d | "X days ago" |
| < ~1 mo | "X weeks ago" |
| < ~1 yr | "X months ago" |
| ≥ ~1 yr | `dd/MM/yyyy` formatted date (locale-default) |
| `null` | "Unknown time" |

ISO8601 parse format: `"yyyy-MM-dd'T'HH:mm:ssXXX"` with `Locale.US`. The `XXX` timezone pattern requires API 24+ (matches `minSdk`).

---

## NavTransitions

Navigation animation presets and `NavHostController` extension functions. All animations: 500 ms, `FastOutSlowInEasing`, ±500 px horizontal slide + fade.

### Transition presets

| Function | Use for | Behaviour |
|---|---|---|
| `slideFromLeftTransitions()` | Home screen | Slides in/out from the **left** on both enter and pop |
| `slideFromRightTransitions()` | Most screens (details, categories, search…) | Slides in from right, out to left; reverses on pop |
| `slideEnterOnlyTransitions()` | Reader, Register, ForgotPassword | Only enter + popExit animated; `exit` and `popEnter` are `null` |

Returns `NavTransitions.NavTransitions` data class with four nullable lambda fields (`enter`, `exit`, `popEnter`, `popExit`). Applied in `NavGraph.kt` via the `composable<Route>` transition params — do not define per-screen transitions outside of `NavGraph`.

### Navigation helpers (extension functions on `NavHostController`)

```kotlin
// Drawer / bottom-nav switches — preserves and restores screen state
fun NavHostController.navigatePreserveState(route: Any)
// popUpTo(startDestination) { saveState = true }, launchSingleTop = true, restoreState = true

// Post-auth navigation — clears the auth back stack entirely
inline fun <reified T : Any> NavHostController.navigateClearStack(route: Any)
// popUpTo<T> { inclusive = true }, launchSingleTop = true
```

`navigateClearStack<T>` takes a **type parameter** `T` — the screen to pop up to (inclusive). Example: after login succeeds, `navigateClearStack<NavRoute.Login>(NavRoute.Home)` removes all auth screens.
