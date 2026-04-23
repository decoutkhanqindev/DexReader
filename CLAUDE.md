# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## IMPORTANT: Keep This File in Sync

**Whenever the project's overall architecture, build setup, or cross-cutting patterns change, this CLAUDE.md must be updated in the same session.** This includes:

- New layer or top-level module added → update Architecture section and package tree
- Build command changed (new Gradle task, new required property) → update Build Commands section
- Exception hierarchy updated (new subtype or rename) → update Exception Hierarchy section
- New Hilt module added or removed → update Hilt DI Modules table
- New utility added to `util/` → update Key Utilities section
- Use case or error-handling pattern changed → update the relevant section

Do not leave this file stale. An outdated CLAUDE.md is worse than no CLAUDE.md.

## Build Commands

```bash
# Build
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew installDebug           # Build and install on connected device/emulator
./gradlew clean                  # Clean build artifacts

# Test
./gradlew testDebugUnitTest                                        # All unit tests
./gradlew testDebugUnitTest --tests "com.example.FooTest"          # Single test class
./gradlew testDebugUnitTest --tests "com.example.FooTest.barTest"  # Single test method
./gradlew connectedAndroidTest                                     # Instrumented tests (device required)
```

`local.properties` must exist at the repo root with `BASE_URL` and `UPLOAD_URL` fields before building. `app/google-services.json` is required for Firebase.

## Architecture

Three-layer Clean Architecture with MVVM in the presentation layer. Uncle Bob's Dependency Rule is strictly enforced — outer layers depend inward, never the reverse.

```
domain/       Pure Kotlin. No Android/framework imports. Entities, exceptions, repository interfaces, use cases.
data/         Implements domain interfaces. Retrofit, Room, Firebase, DataStore, mappers (DTO → domain).
presentation/ Jetpack Compose UI, ViewModels, NavGraph. Uses domain types only via mappers.
di/           4 Hilt modules: LocalModule, RepositoryModule, ApiModule, FirebaseModule.
util/         AsyncHandler, TimeAgo, NavTransitions.
```

### Layer Boundaries

**Domain layer** (`domain/entity/`, `domain/exception/`, `domain/repository/`, `domain/usecase/`):
- No Android dependencies. Pure Kotlin only.
- `User.kt` owns validation: `validateEmail()`, `validatePassword()` etc. throw `ValidationException` subtypes.
- `Chapter.kt` owns nav logic: `Chapter.determineNavPosition(currentId, list, hasNextPage)` → `Chapter.NavPosition`.
- All domain models have `companion object` constants for fallback values (e.g. `Manga.DEFAULT_TITLE`).

**Data layer** (`data/repository/`, `data/mapper/`, `data/network/`, `data/local/`):
- Repository impls use `AsyncHandler.runSuspendCatching(onCatch = { ... })` to remap raw exceptions to `DomainException` subtypes.
- `CancellationException` guard order in repos: `is DomainException -> throw e` first, then remap `HttpException`/`IOException`.
- All mappers are `object` — call as `MangaMapper.toManga(dto)`, never instantiated.
- `ApiParamMapper` converts domain enums (`MangaLanguage`, `MangaSortCriteria`, etc.) to API query strings. ISO codes live here, not in domain.
- Firebase DTOs (`*Request`, `*Response`) retain their own naming. The mapper is the boundary.

**Presentation layer** (`presentation/screens/`, `presentation/model/`, `presentation/mapper/`):
- **Domain types must not appear in composables or UiState.** ViewModels are the only translation boundary.
- UI types: `*UiModel` (data/enum models), `*UiError` (error sealed classes), `*UiState` (screen state).
- `presentation/model/value/` holds presentation-side enums with `@StringRes` (e.g. `MangaLanguageValue`, `CategoryTypeValue`).
- `presentation/mapper/` has 12 mapper objects. `ErrorMapper.toFeatureError()` and `ErrorMapper.toUserError()` are the only exception→UI-error conversion paths.
- `UserMapper` is bidirectional: `User.toUserModel()` + `UserUiModel.toUser()` (needed for `UpdateUserProfileUseCase`).

### Exception Hierarchy

```
DomainException (sealed base)
├── BusinessException (sealed)
│   ├── Auth — InvalidCredentials, UserNotFound, UserAlreadyExists, RegistrationFailed
│   └── Resource — MangaNotFound, ChapterNotFound, ChapterDataNotFound, AccessDenied
├── InfrastructureException (sealed)
│   ├── NetworkUnavailable    ← IOException
│   ├── ServerUnavailable     ← HttpException (4xx/5xx)
│   └── Unexpected
└── ValidationException (sealed)
    ├── Email — Empty, Invalid
    ├── Password — Empty, TooWeak
    ├── ConfirmPassword — Empty, Mismatch
    └── Name — Empty
```

`ValidationException` is thrown in domain model companion methods. All others are mapped at the data layer boundary.

### Use Case Pattern

```kotlin
// One-shot
class GetMangaDetailsUseCase @Inject constructor(...) {
    suspend operator fun invoke(id: String): Result<Manga> =
        AsyncHandler.runSuspendResultCatching { repository.getMangaDetails(id) }
}

// Reactive
class ObserveFavoritesUseCase @Inject constructor(...) {
    operator fun invoke(): Flow<Result<List<FavoriteManga>>> =
        repository.observeFavorites().toFlowResult()
}
```

### Error Handling in ViewModels

`catch (e: CancellationException) { throw e }` must appear **before** any `catch (e: Exception)` block. `toFlowResult()` rethrows `CancellationException` — it will propagate through `collect {}` and hit outer try-catch blocks if not guarded.

### Enum Wiring (Domain → Presentation)

Domain enums (e.g. `MangaLanguage`) map to presentation `*Value` enums (e.g. `MangaLanguageValue`) via `valueOf(name)` — enum names are identical across layers. Conversions belong in `presentation/mapper/`. Composables receive only `*Value` enums and call `stringResource(value.nameRes)` directly.

### Navigation

`NavRoute` is a `sealed interface` with `@Serializable` data objects/classes. `NavGraph.kt` wires all 12 routes to screen composables. Custom slide animations are in `util/NavTransitions.kt`. Use `navigateClearStack()` for auth flows and `navigatePreserveState()` for tab-level navigation.

### Hilt DI Modules

| Module | Provides |
|---|---|
| `LocalModule` | Room `ChapterCacheDatabase`, `ChapterCacheDao`, `DataStore<Preferences>` |
| `RepositoryModule` | All 8 repository interface → impl bindings |
| `ApiModule` | Retrofit, OkHttp (30s timeouts), Moshi + `IsoDateTimeAdapter` |
| `FirebaseModule` | `FirebaseAuth`, `FirebaseFirestore`, 4 Firebase source interface bindings |

All bindings are `@Singleton` in `SingletonComponent`.

### Key Utilities

- **`AsyncHandler`** — `runSuspendResultCatching { }` for use cases; `runSuspendCatching(onCatch)` for repos; `Flow<T>.toFlowResult()` for reactive use cases.
- **`TimeAgo`** — `String.parseIso8601ToEpoch()` and `Long.toTimeAgo()` for human-readable dates.
- **`NavTransitions`** — Pre-built `AnimatedContentTransitionScope` helpers for navigation animations.
