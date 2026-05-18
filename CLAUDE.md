# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## IMPORTANT: Keep This File in Sync

**Whenever architecture, build setup, or any layer's patterns change, this CLAUDE.md must be updated
in the same session.** This includes: new layers/modules, build command changes, exception hierarchy
changes, Hilt module changes, new utilities, use case/error-handling pattern changes, new
screens/composables/UiState/mappers/NavRoutes/Value enums.

## Android CLI & Skills

**Always invoke `android-cli`** for: emulator/AVD, run/deploy app, SDK management, screenshots, UI
layout inspection, docs search.
**Always invoke `android-adb`** for: list devices, install APKs, logcat, push/pull files.
**Always invoke `android-gradle`** for: Gradle build tasks, unit/instrumented tests, dependency
checks.

Do not ask for confirmation before invoking these skills — detect and invoke immediately.

## Git Rules

**All git commands must only be executed when explicitly requested or approved by the user.** Never
run autonomously: `git add/commit/push/checkout/switch/branch/reset/restore/clean`,
`gh pr create/merge`. When in doubt, show the command and ask.

## Build Commands

```bash
./gradlew assembleDebug / assembleRelease / installDebug / clean
./gradlew testDebugUnitTest [--tests "com.example.FooTest.barTest"]
./gradlew connectedAndroidTest   # device required
```

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
util/         AsyncHandler, TimeAgo, NavTransitions.
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

```kotlin
// One-shot
suspend operator fun invoke(id: String): Result<T> =
  AsyncHandler.runSuspendResultCatching { repository.method(id) }

// Reactive
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

**`TimeAgo`**: `String?.parseIso8601ToEpoch(): Long?` | `Long?.toTimeAgo(): String` — both
`SimpleDateFormat` use `ThreadLocal` (not thread-safe without it).

**`NavTransitions`**: `navigatePreserveState(route)` for tab/drawer switches (preserves state);
`navigateClearStack<T>(route)` for auth flows — `T` is the route to pop inclusive (e.g.
`navigateClearStack<NavRoute.Login>(NavRoute.Home)`).

---

## Presentation Layer

Domain types must never appear in composables or `UiState`. ViewModels are the only translation
boundary.

**UiState patterns**:

| Pattern                    | When                       | Used by                                                   |
|----------------------------|----------------------------|-----------------------------------------------------------|
| Sealed interface           | Primary resource load      | `HomeUiState`, `MangaDetailsUiState`, `CategoriesUiState` |
| Data class                 | Form / fine-grained errors | `LoginUiState`, `RegisterUiState`, `ProfileUiState`       |
| `BasePaginationUiState<T>` | Infinite scroll            | CategoryDetails, Favorites, History, Search               |

All UiState/UiModel: `@Immutable`. Lists: `ImmutableList<T>` / `persistentListOf()`.

**Screen split**: `*Screen.kt` (VM injection, `collectAsStateWithLifecycle`) | `*Content.kt` (pure
composable, no VM) | `*ViewModel.kt` (business logic). `*Content` never calls `NavController`
directly.

**Navigation**: `NavRoute` sealed interface with `@Serializable` members. `navigateClearStack()` for
auth flows; `navigatePreserveState()` for tab/drawer navigation.

**`rememberSaveable` policy**: `MainActivity` sets `SCREEN_ORIENTATION_PORTRAIT` — rotation never
occurs. Use `remember` for all composition-scoped UI state. `rememberSaveable` only when
process-death survival is explicitly required (scroll position, form text mid-fill, reader
fullscreen toggle).

**Error dialog state**: `remember { mutableStateOf(true) }` +
`LaunchedEffect(uiState) { if (uiState is Error) isShowErrorDialog = true }`. Never
`rememberSaveable` for pagination-screen error dialogs.

**Compose conventions**:

- `collectAsStateWithLifecycle()` not `collectAsState()`
- `OutlinedTextFieldDefaults.colors()` is `@Composable` — cannot be in `remember { }`. Use
  `val colorScheme = MaterialTheme.colorScheme`.
- All LazyList items: stable keys (`key = MangaModel::id`) — required, no exceptions
- `derivedStateOf { }` for scroll-driven booleans:
  `val show by remember { derivedStateOf { state.firstVisibleItemIndex > 0 } }`
- All `@Preview` wrapped in `DexReaderTheme { }`

**Compose performance rules**:

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

