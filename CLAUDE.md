# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

---

## Agent Rules

### Comments

**Never add comments** — no `//`, `/* */`, or KDoc (`/** */`) — unless explicitly requested by the
user. This applies globally across all layers (domain, data, presentation, util, baselineprofile).
Code should be self-explanatory through naming.

### Git

**All git commands must only be executed when explicitly requested or approved by the user.** Never
run autonomously: `git add/commit/push/checkout/switch/branch/reset/restore/clean`,
`gh pr create/merge`. When in doubt, show the command and ask.

### Skills

**Always invoke `android-cli`** for: emulator/AVD, run/deploy app, SDK management, screenshots, UI
layout inspection, docs search.
**Always invoke `android-adb`** for: list devices, install APKs, logcat, push/pull files.
**Always invoke `android-gradle`** for: Gradle build tasks, unit/instrumented tests, dependency
checks.

Do not ask for confirmation before invoking these skills — detect and invoke immediately.

---

## Project Setup

### Build Commands

```bash
./gradlew assembleDebug / assembleRelease / installDebug / clean
./gradlew testDebugUnitTest [--tests "com.example.FooTest.barTest"]
./gradlew connectedAndroidTest   # device required
```

### Required Config

`local.properties` must have `BASE_URL` + `UPLOAD_URL`. `app/google-services.json` required for
Firebase.

---

## Architecture

Three-layer Clean Architecture + MVVM. Dependency Rule: outer layers depend inward only.

```
domain/       Pure Kotlin. Entities, exceptions, repository interfaces, use cases.
data/         Implements domain interfaces. Retrofit, Room, Firebase, DataStore.
presentation/ Jetpack Compose UI, ViewModels, NavGraph.
di/           4 Hilt modules: LocalModule, RepositoryModule, ApiModule, FirebaseModule.
util/         AsyncHandler, DateTimeHandler, NavTransitions.
```

---

## Domain Layer

Zero Android imports. `ValidationException` is thrown in `User` companion methods only — all other
exceptions are mapped at the data boundary.

**Business logic in companions** — call from VMs, never re-implement:

- `Chapter.determineNavPosition(currentId, list, hasNextPage): NavPosition` — sets prev/next chapter
  IDs
- `Chapter.isPrefetchNextPage(currentIndex, listSize): Boolean` — true within last 5 items
- `ReadingHistory.generateId(mangaId, chapterId): String` — `"${mangaId}_${chapterId}"`
- `ReadingHistory.findContinueTarget(list): ReadingHistory?` — first unfinished session
- `ReadingHistory.findInitialPage(chapterId, navChapterId, navPage, list): Int`
- `User.validateEmail/Password/ConfirmPassword/Name()` — throw `ValidationException` subtypes

**UNKNOWN rule**: never discard unrecognized API values — map to `UNKNOWN` enum entry.
**Default params**: repository interfaces define defaults; impls must not redefine them.

### Exception Hierarchy

```
DomainException
├── ValidationException — Email.{Empty|Invalid}, Password.{Empty|TooWeak},
│                         ConfirmPassword.{Empty|Mismatch}, Name.Empty
├── BusinessException  — Auth.{InvalidCredentials|UserNotFound|UserAlreadyExists|RegistrationFailed}
│                         Resource.{MangaNotFound|ChapterNotFound|ChapterDataNotFound|AccessDenied}
└── InfrastructureException — NetworkUnavailable (IOException), ServerUnavailable (HttpException), Unexpected
```

### Use Case Pattern

One-shot:

```kotlin
suspend operator fun invoke(id: String): Result<T> =
  AsyncHandler.runSuspendResultCatching { repository.method(id) }
```

Reactive:

```kotlin
operator fun invoke(): Flow<Result<List<T>>> = repository.observe().toFlowResult()
```

---

## Data Layer

**Exception mapping** — choose by call site:

| Function                             | Use for                                             |
|--------------------------------------|-----------------------------------------------------|
| `toDomainException()`                | Retrofit catch blocks (HTTP + IO)                   |
| `toUnexpectedException()`            | Room / generic suspend catch; Auth Flow `.catch {}` |
| `toFirebaseFirestoreException()`     | Firestore **suspend** write/delete                  |
| `toFirebaseFirestoreFlowException()` | Firestore **Flow** `.catch {}`                      |
| `toFirebaseAuthException()`          | Firebase Auth suspend operations                    |

All functions rethrow `DomainException` and `CancellationException` unchanged.
**`CancellationException` guard in repos**: `is DomainException -> throw e` first, then remap
`HttpException`/`IOException`.

**Firebase Request DTOs**: `id` is `@get:Exclude` (becomes document ID); fields use
`@get:PropertyName("snake_case")`; timestamps use `@ServerTimestamp val field: Date? = null`.

**Firebase Response DTOs**: all fields are `var` (required for reflection); `id` is
`@get:Exclude @set:Exclude`, populated via `doc.toObject(...)?.copy(id = doc.id)`.

**Firestore paths**: `/users/{userId}` | `/users/{userId}/favorites/{mangaId}` |
`/users/{userId}/history/{historyId}`

**Cursor pagination**: all paginated Firestore queries use `startAfter(lastDocument).limit(n)` —
`null` lastItemId = first page.

**Enum wiring**: domain enums → `*Value` enums via `valueOf(name)` — names are identical across
layers. `ApiParamMapper` owns ISO codes/API strings; never put them in domain enums.

**Mappers**: all `object` singletons — `MangaMapper.toManga(dto)`, never instantiated. `UserMapper`
is the only bidirectional mapper (needed by `UpdateUserProfileUseCase`).

---

## DI Layer

4 modules, all `@InstallIn(SingletonComponent::class)`, all `@Singleton`.

| Module             | Type            | Provides                                                                 |
|--------------------|-----------------|--------------------------------------------------------------------------|
| `LocalModule`      | `object`        | Room `ChapterCacheDatabase`, `ChapterCacheDao`, `DataStore<Preferences>` |
| `RepositoryModule` | **`interface`** | 8 `@Binds` for all repository interface → impl bindings                  |
| `ApiModule`        | `object`        | Moshi, OkHttp (30s timeouts), Retrofit, `ApiService`                     |
| `FirebaseModule`   | `object`        | `FirebaseAuth`, `FirebaseFirestore`, 4 Firebase source `@Provides`       |

**Critical rules**:

- `RepositoryModule` is an **`interface`** (not `object`) — `@Binds` requires abstract functions
- Firebase sources use `@Provides` not `@Binds` — impls depend on `FirebaseAuth`/`FirebaseFirestore`
  provided in the same module
- **Moshi adapter order**: `IsoDateTimeAdapter` before `KotlinJsonAdapterFactory` — custom adapters
  must register first
- `NetworkInterceptor` is auto-resolved by Hilt — never add an explicit `@Provides` (causes
  duplicate binding error)
- `fallbackToDestructiveMigration(true)` safe only for `ChapterCacheDatabase` (re-fetchable cache);
  never for Firestore user data

---

## Util

**`AsyncHandler`** — function selection:

- `runSuspendResultCatching { }` → `Result<T>`, catches `Throwable` — use in **use cases**
- `runSuspendCatching(context, block, catch)` → `T` directly, catches `Exception` only — use in *
  *repos** with exception remapping
- `Flow<T>.toFlowResult()` → `Flow<Result<T>>` — rethrows `CancellationException` — use in *
  *reactive use cases**

**`CancellationException` guard**: must appear **before** any `catch (e: Exception)` in VMs —
`toFlowResult()` rethrows it through `collect { }`.

**`DateTimeHandler`**: `String?.parseIso8601ToEpoch(): Long?` | `Long?.toTimeAgo(): String` — both
`SimpleDateFormat` use `ThreadLocal` (not thread-safe without it).

**`NavTransitions`**: `navigatePreserveState<Root>(route)` for tab/drawer switches (preserves state)
via `popUpTo<Root> { saveState = true }` + `restoreState = true` — `Root` MUST be the actual tab-root
destination that stays on the back stack forever (`NavRoute.Home`), never the `NavHost`'s graph-level
`startDestination` (`NavRoute.Splash`) — Splash is popped inclusively right after login, so a
`popUpTo` targeting it can never match again and save/restore silently no-ops, losing tab state on
every switch. `navigateClearStack<T>(route)` for auth flows — `T` is the route to pop inclusive (e.g.
`navigateClearStack<NavRoute.Login>(NavRoute.Home)`).

---

## Presentation Layer

Domain types must never appear in composables or `UiState`. ViewModels are the only translation
boundary.

### UiState Patterns

| Pattern                    | When                       | Used by                                                   |
|----------------------------|----------------------------|-----------------------------------------------------------|
| Sealed interface           | Primary resource load      | `HomeUiState`, `MangaDetailsUiState`, `CategoriesUiState` |
| Data class                 | Form / fine-grained errors | `LoginUiState`, `RegisterUiState`, `ProfileUiState`       |
| `BasePaginationUiState<T>` | Infinite scroll            | CategoryDetails, Favorites, History, Search               |

All UiState/UiModel: `@Immutable`. Lists: `ImmutableList<T>` / `persistentListOf()`.

### Screen Structure

**Screen split**: `*Screen.kt` (VM injection, `collectAsStateWithLifecycle`) | `*Content.kt` (pure
composable, no VM) | `*ViewModel.kt` (business logic). `*Content` never calls `NavController`
directly.

**Navigation**: `NavRoute` sealed interface with `@Serializable` members. `navigateClearStack()` for
auth flows; `navigatePreserveState()` for tab/drawer navigation.

### State Management

**Error dialog state**: `remember { mutableStateOf(false) }` +
`LaunchedEffect(uiState) { if (uiState is Error) isShowErrorDialog = true }` — never key `remember` on
`uiState` (or a field of it) to derive the initial/show value; only `LaunchedEffect` may set it back to
`true`, so dismiss stays dismissed until the tracked condition flips again. For bundled (non-sealed)
UiState, key the `LaunchedEffect` on the specific boolean field (`uiState.isError`), not the whole
state object, so unrelated field changes (e.g. text input) don't re-arm the dialog.

### Compose Conventions

- No top-level `private val` in composable files — define variables directly inside the composable.
  Exception: theme tokens shared across multiple composables belong in `presentation/theme/`.
- `collectAsStateWithLifecycle()` not `collectAsState()`
- `OutlinedTextFieldDefaults.colors()` is `@Composable` — cannot be in `remember { }`. Use
  `val colorScheme = MaterialTheme.colorScheme`.
- All LazyList items: stable keys (`key = MangaModel::id`) — required whenever the list has a real
  unique ID. If items are plain values with no guaranteed uniqueness (e.g. raw search-suggestion
  strings), omit `key` entirely rather than key on the value or its `hashCode()` — duplicate keys
  crash the list at runtime (`IllegalArgumentException: Key "..." was already used`)
- `derivedStateOf { }` for scroll-driven booleans:
  `val show by remember { derivedStateOf { state.firstVisibleItemIndex > 0 } }`
- All `@Preview` wrapped in `DexReaderTheme { }`
- `Modifier.blur()` requires API 31+ (RenderEffect) — below that (app `minSdk = 24`) it silently
  no-ops, and it's a heavier hardware layer than the alternative even when it does run. Never use it
  for loading/dim overlays — use `Modifier.blurBackground(topAlpha, bottomAlpha)` (gradient, works on
  every API level, cheaper), the established pattern across every screen (Login, Register,
  ForgotPassword, History, Profile, Settings, MangaDetails)
- Coil `ImageRequest` passed to `AsyncImage` / `ZoomableAsyncImage` must be
  `remember(url) { ImageRequest.Builder(...).build() }` — never built inline in the call site.
  Established in `MangaCoverArt`, `ChapterPageImage`, `MangaDetailsBackground`, `ProfilePicture`

### Compose Performance

- `viewModel::method` is NOT auto-memoized — always `remember { viewModel::method }`
- LazyList keys: never include index — stable server-side ID only
- Hoist `remember(list) { list.associateBy { it.id } }` before `items { }` — never `find { }` inside
- No backwards writes: never write to `MutableState` already read in the same composition pass
- `remember` keys must include ALL captured values that can change —
  `remember(item.id) { { cb(item.page) } }` silently stales if `page` changes but is not in the key
- `LazyListScope.items { }` / `LazyGridScope.items { }` are NOT `@Composable` — strong skipping does
  not auto-memoize lambdas inside; forward params directly or wrap with `remember(key) { }` per item
- `inline forEach` inside a `@Composable` content lambda runs in `@Composable` scope — strong
  skipping auto-memoizes; key per stable value: `remember(entry) { { cb(entry) } }`
- `derivedStateOf { }` only tracks `State<T>` reads — read `uiState.field` (through the State
  delegate), never a bare composable parameter; bare params are invisible to the tracker
- State scoped to a single page/item of `HorizontalPager` / `LazyColumn` / `LazyRow` (e.g.
  `isImageLoaded`) must be declared **inside** that content lambda, keyed per item
  (`remember(item.id) { mutableStateOf(...) }`) — never hoisted above the pager/list. Hoisting shares
  one instance across every page/item (`MangaBanner` bug: one shared flag turned off shimmer on every
  banner page as soon as a single image finished loading, and re-triggered recomposition of every
  composed page on each toggle)
- Custom `graphicsLayer`-driven animation modifiers (`animateItemOnAppear`, `onScalableClick` in
  `common/Modifier.kt`) must read the animated `State<Float>` via `.value` **inside** the
  `graphicsLayer { }` block — never destructure via `by` at the top of the function. A `by` read
  there re-triggers full recomposition on every animation frame instead of a cheap redraw-only pass

---

## Maintenance

**Whenever architecture, build setup, or any layer's patterns change, this CLAUDE.md must be updated
in the same session.** This includes: new layers/modules, build command changes, exception hierarchy
changes, Hilt module changes, new utilities, use case/error-handling pattern changes, new
screens/composables/UiState/mappers/NavRoutes/Value enums.

**Large multi-file sessions** (e.g. a codebase-wide audit) log a dated entry in `CHANGELOG.md` at the
repo root — date, title, what changed. Append new entries there rather than creating new one-off
report files per session.
