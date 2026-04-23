# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## IMPORTANT: Keep This File in Sync

**Whenever any file in the `domain/` layer is added, removed, or modified, this CLAUDE.md must be updated in the same session to reflect the change.** This includes:

- New entity added or removed → update Package Structure and entity reference
- Entity field added, removed, or renamed → update the entity's field list
- New companion method or validation rule added → update the relevant entity section
- New `DomainException` subtype added or removed → update Exception Hierarchy
- New repository interface added → update Package Structure
- New use case added or removed → update Package Structure and use case count
- Use case pattern changed (signature, return type) → update Use Case Pattern section

Do not leave this file stale. An outdated CLAUDE.md is worse than no CLAUDE.md.

## Domain Layer Rules

This layer is **pure Kotlin only** — zero Android/framework imports. No Hilt, no Retrofit, no Room, no Compose. Any dependency on an Android class is a CA violation.

### Package Structure

```
domain/
├── entity/          Data models (7 entities + 7 value enums)
│   ├── category/    Category.kt, value/CategoryType.kt
│   ├── manga/       Manga.kt, Chapter.kt, ChapterPages.kt, FavoriteManga.kt
│   │                value/MangaLanguage.kt, MangaStatus.kt, MangaContentRating.kt
│   │                value/MangaSortCriteria.kt, MangaSortOrder.kt
│   ├── user/        User.kt, ReadingHistory.kt
│   └── settings/    value/ThemeMode.kt
├── exception/       Sealed exception hierarchy (4 files)
├── repository/      Interfaces only — 8 files, no implementations
└── usecase/         27 use cases grouped by feature
    ├── category/    GetCategoryListUseCase, GetMangaListByCategoryUseCase
    ├── manga/       8 use cases (get, search, chapter, pages, suggestions)
    ├── manga/cache/ 4 cache use cases
    ├── settings/    ObserveThemeModeUseCase, SaveThemeModeUseCase
    └── user/        auth/ (4), favorite/ (4), history/ (3), profile/ (3)
```

---

## Entity Models

All models are `data class`. All have a `companion object` with fallback constants — mappers must use these instead of raw string/null literals.

### Entities with business logic in companion

**`Chapter`** — nav position computation lives here, not in VMs:
```kotlin
Chapter.determineNavPosition(currentChapterId, chapterList, hasNextPage): NavPosition
Chapter.isPrefetchNextPage(currentIndex, listSize): Boolean  // threshold = 5
```
`NavPosition` fields: `foundAtIndex`, `previousChapterId`, `nextChapterId`, `canNavigatePrevious`, `canNavigateNext`

**`ReadingHistory`** — reading session logic lives here:
```kotlin
ReadingHistory.generateId(mangaId, chapterId): String
ReadingHistory.findContinueTarget(historyList): ReadingHistory?
ReadingHistory.findInitialPage(chapterId, navChapterId, navPage, historyList): Int
```

**`User`** — validation lives here, throws `ValidationException` subtypes:
```kotlin
User.validateEmail(email)           // throws ValidationException.Email.Empty / .Invalid
User.validatePassword(password)     // throws ValidationException.Password.Empty / .TooWeak
User.validateConfirmPassword(p, cp) // throws ValidationException.ConfirmPassword.Empty / .Mismatch
User.validateName(name)             // throws ValidationException.Name.Empty
```
Password minimum length: 8 characters.

**`ChapterPages`** — `totalPages: Int` is a computed property (not stored).

### Intentionally-kept naming

These property names look like DTO names but were deliberately kept for disambiguation:
- `ReadingHistory.chapterNumber`, `.chapterVolume`, `.mangaCoverUrl` — prefix distinguishes from manga-level fields
- `ChapterPages.baseUrl` — team preference

---

## Exception Hierarchy

```
DomainException (sealed)
├── BusinessException (sealed)
│   ├── Auth (sealed) — InvalidCredentials, UserNotFound, UserAlreadyExists, RegistrationFailed
│   └── Resource (sealed) — MangaNotFound, ChapterNotFound, ChapterDataNotFound, AccessDenied
├── InfrastructureException (sealed) — NetworkUnavailable, ServerUnavailable, Unexpected
└── ValidationException (sealed)
    ├── Email — Empty, Invalid (data objects)
    ├── Password — Empty, TooWeak (data objects)
    ├── ConfirmPassword — Empty, Mismatch (data objects)
    └── Name — Empty (data object)
```

**Where each type is thrown:**
- `ValidationException` — thrown in `User` companion validation methods (domain layer)
- `BusinessException.Auth` — mapped from Firebase auth errors (data layer)
- `BusinessException.Resource` — mapped from API null responses (data layer)
- `InfrastructureException.NetworkUnavailable` — mapped from `IOException` (data layer)
- `InfrastructureException.ServerUnavailable` — mapped from `HttpException` (data layer)

Do not throw `DomainException` subtypes directly inside the domain layer (except `ValidationException` from `User`). All other mapping is done at data layer boundaries.

---

## Repository Interfaces

All repositories are interfaces. Implementations are in the data layer. Default parameter values are defined here — data layer implementations must not redefine them.

| Repository | Return pattern |
|---|---|
| `MangaRepository` | `suspend fun → T` (one-shot) |
| `ChapterRepository` | `suspend fun → T` (one-shot) |
| `CategoryRepository` | `suspend fun → T` (one-shot) |
| `CacheRepository` | `suspend fun → T` (one-shot) |
| `SettingsRepository` | `fun observeThemeMode(): Flow<T>` + `suspend fun save` |
| `FavoritesRepository` | `fun observe*(): Flow<T>` + `suspend fun` write ops |
| `HistoryRepository` | `fun observeHistory(): Flow<T>` + `suspend fun` write ops |
| `UserRepository` | mixed — `fun observe*(): Flow<T>` + `suspend fun` auth/write ops |

Default values for pagination: `limit = 20`, `offset = 0`. Default language: `MangaLanguage.ENGLISH`. Default sort: `MangaSortCriteria.LATEST_UPDATE` / `MangaSortOrder.DESC`.

---

## Use Case Patterns

**One-shot** (returns `Result<T>`):
```kotlin
class GetMangaDetailsUseCase @Inject constructor(private val repository: MangaRepository) {
    suspend operator fun invoke(mangaId: String): Result<Manga> =
        AsyncHandler.runSuspendResultCatching { repository.getMangaDetails(mangaId) }
}
```

**Reactive** (returns `Flow<Result<T>>`):
```kotlin
class ObserveFavoritesUseCase @Inject constructor(private val repository: FavoritesRepository) {
    operator fun invoke(userId: String, ...): Flow<Result<List<FavoriteManga>>> =
        repository.observeFavorites(userId, ...).toFlowResult()
}
```

**With validation** — use cases call `User.validate*()` before the repository call. Validation exceptions propagate as-is inside the `runSuspendResultCatching` block and become `Result.failure`.

**`AddToFavoritesUseCase`** — the only use case that constructs a domain entity (`FavoriteManga`) from another (`Manga`). This construction is intentional — it's business logic, not mapping.

**`UpsertHistoryUseCase`** — takes flat primitive parameters (not a `ReadingHistory` object) because the caller (VM) should not construct domain entities directly.

**`UpdateUserProfileUseCase`** — takes `(currentUser: User, newName: String?, newAvatarUrl: String?)`, validates name if changed, returns early (`Result.success(Unit)`) if nothing changed.

**`ClearExpiredCacheUseCase`** — cache expiry is `24 * 60 * 60 * 1000` ms (24 hours), defined as a private constant inside the use case.

**`GetCategoryListUseCase`** — returns `Result<Map<CategoryType, List<Category>>>` (already grouped). The grouping logic lives inside this use case, not in the VM.

**`GetMangaSuggestionsUseCase`** — suggestion result limit is `10`, defined as a private constant inside the use case.
