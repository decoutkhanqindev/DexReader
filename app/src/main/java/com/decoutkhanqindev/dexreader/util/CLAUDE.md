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

### Function Selection Guide

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

### Critical Catch-Type Distinction

**`runSuspendResultCatching` catches `Throwable`** — this includes `Error` subclasses (e.g. `OutOfMemoryError`). Everything that isn't `CancellationException` becomes `Result.failure`.

**`runSuspendCatching` catches `Exception` only** — `Error` subclasses propagate uncaught. This is intentional: repository-layer exception mapping (`onCatch`) should only handle recoverable `Exception` subtypes.

**`toFlowResult()` is not a suspend function** — it's a synchronous Flow transform. The `.catch` operator handles emissions-level exceptions and rethrows `CancellationException`.

### Key Notes

`runSuspendCatching` without a `catch` lambda is equivalent to `withContext` — prefer `withContext` directly in that case.

`toFlowResult()` rethrows `CancellationException` from the `.catch` operator — it does **not** absorb it. A `CancellationException` rethrown by `toFlowResult` will propagate through `collect { }` in ViewModels and hit any outer `try/catch`. Always add a `catch (c: CancellationException) { throw c }` guard before `catch (e: Exception)` in VM coroutines.

---

## TimeAgo

Date formatting `object`. Both `SimpleDateFormat` instances use `ThreadLocal` — `SimpleDateFormat` is not thread-safe.

```kotlin
fun String?.parseIso8601ToEpoch(): Long?   // ISO8601 string → epoch millis; returns null on parse failure
fun Long?.toTimeAgo(): String              // epoch millis → human-readable relative string
```

**`toTimeAgo()` thresholds (exact ms values from source):**

| diff value | Threshold | Output |
|---|---|---|
| diff < 0 | negative | `"Unknown time"` |
| diff < 60_000 | 60 s | `"X seconds ago"` |
| diff < 3_600_000 | 1 h | `"X minutes ago"` |
| diff < 86_400_000 | 24 h | `"X hours ago"` |
| diff < 604_800_000 | 7 d | `"X days ago"` |
| diff < 2_629_746_000 | ~30.44 d | `"X weeks ago"` |
| diff < 31_556_926_000 | ~365.24 d | `"X months ago"` |
| else | ≥ ~1 yr | `dd/MM/yyyy` formatted date |
| `null` input | — | `"Unknown time"` |

**Format details:**
- **ISO 8601 parse:** `"yyyy-MM-dd'T'HH:mm:ssXXX"` with `Locale.US`. The `XXX` timezone pattern requires API 24+ (matches `minSdk`). Silent catch on any parse failure — returns `null`.
- **Date display:** `"dd/MM/yyyy"` with `Locale.getDefault()` (not `Locale.US` — locale-aware).
- Both `SimpleDateFormat` instances are wrapped in `ThreadLocal.withInitial { }` for thread safety.

---

## NavTransitions

Navigation animation presets and `NavHostController` extension functions.

**Animation constants (private):**
- `ANIMATION_DURATION = 500` ms
- `OFFSET_X = 500` px

**Animation specs:**
- `slideAnimationSpec` — `tween(500, easing = FastOutSlowInEasing)` for `IntOffset`
- `fadeAnimationSpec` — `tween(500)` with **default linear easing** (not FastOutSlowInEasing)

### Transition Presets

| Function | Screens | enter | exit | popEnter | popExit |
|---|---|---|---|---|---|
| `slideFromLeftTransitions()` | Home | −500 | −500 | −500 | −500 |
| `slideFromRightTransitions()` | Details, Categories, Search, … | +500 | −500 | −500 | +500 |
| `slideEnterOnlyTransitions()` | Reader, Register, ForgotPassword | +500 | `null` | `null` | +500 |

Offsets are `initialOffsetX` / `targetOffsetX` in pixels. Positive = right side; negative = left side.

All presets combine a horizontal slide with a fade (`slideIn + fadeIn` / `slideOut + fadeOut`).

Returns `NavTransitions.NavTransitions` data class with four nullable lambda fields:
```kotlin
data class NavTransitions(
    val enter:    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val exit:     (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val popExit:  (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
)
```

Applied in `NavGraph.kt` via the `composable<Route>` transition params — do not define per-screen transitions outside of `NavGraph`.

### Navigation Helpers (Extension Functions on `NavHostController`)

```kotlin
// Drawer / bottom-nav switches — preserves and restores screen state
fun NavHostController.navigatePreserveState(route: Any)
// popUpTo(graph.startDestinationId) { saveState = true }, launchSingleTop = true, restoreState = true

// Post-auth navigation — clears the auth back stack entirely
inline fun <reified T : Any> NavHostController.navigateClearStack(route: Any)
// popUpTo<T> { inclusive = true }, launchSingleTop = true
```

**Key distinction between the two helpers:**
- `navigatePreserveState` uses `graph.startDestinationId` (an `Int`) — always pops to the graph's start, regardless of type. Used for menu/drawer navigation where state must be preserved.
- `navigateClearStack<T>` uses a reified type parameter `T` — pops to (and removes) a specific named route. Used for auth flows: `navigateClearStack<NavRoute.Login>(NavRoute.Home)` removes `Login` from the back stack.

`navigateClearStack<T>` takes a **type parameter** `T` — the screen to pop up to (inclusive). Example: after login succeeds, `navigateClearStack<NavRoute.Login>(NavRoute.Home)` removes all auth screens.
