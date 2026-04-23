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
└── usecase/         32 use cases grouped by feature
    ├── manga/       10 use cases (home lists, details, chapter, pages, search, suggestions)
    ├── manga/cache/ 4 cache use cases
    ├── category/    2 use cases
    ├── settings/    2 use cases
    └── user/        auth/ (4), profile/ (3), favorite/ (4), history/ (3)
```

---

## Entity Models

All models are `data class`. All have a `companion object` with fallback constants — mappers must use these instead of raw string/null literals.

### Field Reference

```kotlin
data class Manga(
    val id: String,
    val title: String,
    val coverUrl: String,
    val description: String?,
    val author: String?,
    val artist: String?,
    val categories: List<Category>,
    val status: MangaStatus,
    val contentRating: MangaContentRating,
    val year: String?,
    val availableLanguages: List<MangaLanguage>,
    val latestChapter: String?,
    val updatedAt: Long?,          // epoch millis; null if not available
)

data class Chapter(
    val id: String,
    val mangaId: String,
    val title: String?,
    val number: String?,
    val volume: String?,
    val publishedAt: Long?,        // epoch millis
    val language: MangaLanguage,
)

data class ChapterPages(
    val chapterId: String,
    val mangaId: String,
    val baseUrl: String,
    val dataHash: String,
    val pages: List<String>,       // relative page filenames
) {
    val totalPages: Int get() = pages.size  // computed, not stored
}

data class FavoriteManga(
    val id: String,
    val title: String,
    val coverUrl: String,
    val author: String,
    val status: MangaStatus,
    val addedAt: Long?,            // epoch millis; null = backend assigns
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
)

data class ReadingHistory(
    val id: String,
    val mangaId: String,
    val mangaTitle: String,
    val mangaCoverUrl: String,
    val chapterId: String,
    val chapterTitle: String,
    val chapterNumber: String,     // "chapter" prefix disambiguates from manga-level fields
    val chapterVolume: String,     // "chapter" prefix disambiguates from manga-level fields
    val lastReadPage: Int,
    val pageCount: Int,
    val lastReadAt: Long?,         // epoch millis
)

data class Category(
    val id: String,
    val title: String,
    val type: CategoryType,
)
```

### Companion Object Constants

```
Manga.DEFAULT_TITLE          = "Untitled"
Manga.DEFAULT_DESCRIPTION    = "No description"
Manga.DEFAULT_AUTHOR         = "Unknown"
Manga.DEFAULT_ARTIST         = "Unknown"
Manga.DEFAULT_YEAR           = "Unknown"
Manga.DEFAULT_LAST_CHAPTER   = "Unknown"
Manga.DEFAULT_COVER_URL      = ""

Chapter.DEFAULT_TITLE          = "Untitled"
Chapter.DEFAULT_CHAPTER_NUMBER = "0"
Chapter.DEFAULT_VOLUME         = "0"
// private: Chapter.PREFETCH_THRESHOLD = 5  (used by isPrefetchNextPage)

User.DEFAULT_NAME  = ""
User.DEFAULT_EMAIL = ""
// private: User.MIN_PASSWORD_LENGTH = 8
// private: User.EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[a-zA-Z]{2,}$"

ReadingHistory.DEFAULT_LAST_READ_AT = null
// private: ReadingHistory.FIRST_PAGE = 1

Category.DEFAULT_TITLE = "Unknown"
Category.DEFAULT_TYPE  = CategoryType.UNKNOWN

// FavoriteManga — no companion constants
// ChapterPages   — no companion constants
```

### Entities with Business Logic in Companion

**`Chapter`** — nav position computation lives here, not in VMs:
```kotlin
Chapter.determineNavPosition(currentChapterId: String, chapterList: List<Chapter>, hasNextPage: Boolean): NavPosition
Chapter.isPrefetchNextPage(currentIndex: Int, listSize: Int): Boolean  // true if within last 5 items
```
`NavPosition` fields: `foundAtIndex: Int`, `previousChapterId: String?`, `nextChapterId: String?`, `canNavigatePrevious: Boolean`, `canNavigateNext: Boolean`

`determineNavPosition` behavior:
- If chapter not found in list: `foundAtIndex = -1`, both IDs null, `canNavigateNext = hasNextPage`
- If found: sets previous/next IDs, `canNavigateNext = !isLast || hasNextPage`

**`ReadingHistory`** — reading session logic lives here:
```kotlin
ReadingHistory.generateId(mangaId: String, chapterId: String): String         // returns "${mangaId}_${chapterId}"
ReadingHistory.findContinueTarget(historyList: List<ReadingHistory>): ReadingHistory?  // first unfinished session
ReadingHistory.findInitialPage(chapterId: String, navChapterId: String, navPage: Int, historyList: List<ReadingHistory>): Int
```
`findContinueTarget` — returns first entry where `lastReadPage < pageCount`; falls back to first in list if all finished.

`findInitialPage` — returns `navPage` if `chapterId == navChapterId && navPage > 0` (preserve nav state); otherwise returns `lastReadPage` from history, or `FIRST_PAGE` (1) if not in history.

**`User`** — validation lives here, throws `ValidationException` subtypes:
```kotlin
User.validateEmail(email: String)                                        // throws Email.Empty / .Invalid
User.validatePassword(password: String)                                  // throws Password.Empty / .TooWeak
User.validateConfirmPassword(password: String, confirmPassword: String)  // throws ConfirmPassword.Empty / .Mismatch
User.validateName(name: String)                                          // throws Name.Empty
```
Password minimum length: 8 characters (`MIN_PASSWORD_LENGTH`).

**`ChapterPages`** — `totalPages: Int` is a computed property (`pages.size`), not stored.

### Intentionally-Kept Naming

These property names look like DTO names but were deliberately kept for disambiguation:
- `ReadingHistory.chapterNumber`, `.chapterVolume`, `.mangaCoverUrl` — prefix distinguishes from manga-level fields
- `ChapterPages.baseUrl` — team preference

---

## Value Enums

All value enums are in `entity/value/`. No companion objects — they are pure enum constants.

```
MangaStatus        : ON_GOING, COMPLETED, HIATUS, CANCELLED, UNKNOWN
MangaContentRating : SAFE, SUGGESTIVE, EROTICA, UNKNOWN
MangaSortCriteria  : LATEST_UPDATE, TRENDING, MOST_VIEWED, TOP_RATED
MangaSortOrder     : ASC, DESC
CategoryType       : GENRE, THEME, FORMAT, CONTENT, UNKNOWN
ThemeMode          : SYSTEM, DARK, LIGHT
MangaLanguage      : 64 entries — ENGLISH is the default; full list covers all MangaDex languages
                     (ENGLISH, JAPANESE, KOREAN, CHINESE_SIMPLIFIED, CHINESE_TRADITIONAL,
                      FRENCH, GERMAN, SPANISH, SPANISH_LATAM, PORTUGUESE_BR, RUSSIAN,
                      ARABIC, VIETNAMESE, INDONESIAN, THAI, TURKISH, POLISH, ITALIAN,
                      and 46 more)
```

`UNKNOWN` entries exist in every status/rating/type enum — mappers must never silently discard unrecognised API values; map them to `UNKNOWN` instead.

---

## Exception Hierarchy

```
DomainException (sealed, base)
├── ValidationException (sealed)           ← subtypes are data objects — no cause param
│   ├── Email (sealed)
│   │   ├── data object Empty              "Email must not be empty"
│   │   └── data object Invalid            "Email format is invalid"
│   ├── Password (sealed)
│   │   ├── data object Empty              "Password must not be empty"
│   │   └── data object TooWeak            "Password must be at least 8 characters"
│   ├── ConfirmPassword (sealed)
│   │   ├── data object Empty              "Confirm password must not be empty"
│   │   └── data object Mismatch           "Passwords do not match"
│   └── Name (sealed)
│       └── data object Empty              "Name must not be empty"
├── BusinessException (sealed)             ← subtypes are classes with cause: Throwable? = null
│   ├── Auth (sealed)
│   │   ├── class InvalidCredentials       "Invalid email or password"
│   │   ├── class UserNotFound             "User account not found"
│   │   ├── class UserAlreadyExists        "Email is already registered"
│   │   └── class RegistrationFailed       "Registration failed"
│   └── Resource (sealed)
│       ├── class MangaNotFound            "Manga not found"
│       ├── class ChapterNotFound          "Chapter not found"
│       ├── class ChapterDataNotFound      "Chapter data not found"
│       └── class AccessDenied             "Access denied"
└── InfrastructureException (sealed)       ← subtypes are classes with cause: Throwable? = null
    ├── class NetworkUnavailable           "No internet connection"         ← IOException
    ├── class ServerUnavailable            "Server is not responding"       ← HttpException (4xx/5xx)
    └── class Unexpected                   "An unexpected error occurred"
```

**Where each type is thrown:**
- `ValidationException` — thrown in `User` companion validation methods (domain layer only)
- `BusinessException.Auth` — mapped from Firebase auth errors (data layer)
- `BusinessException.Resource` — mapped from API null responses (data layer)
- `InfrastructureException.NetworkUnavailable` — mapped from `IOException` (data layer)
- `InfrastructureException.ServerUnavailable` — mapped from `HttpException` (data layer)

Do not throw `DomainException` subtypes directly inside the domain layer (except `ValidationException` from `User`). All other mapping is done at data layer boundaries.

---

## Repository Interfaces

All repositories are interfaces. Implementations are in the data layer. **Default parameter values are defined here — data layer implementations must not redefine them.**

```kotlin
// MangaRepository
suspend fun getLatestUpdateMangaList(): List<Manga>
suspend fun getTrendingMangaList(): List<Manga>
suspend fun getNewReleaseMangaList(): List<Manga>
suspend fun getTopRatedMangaList(): List<Manga>
suspend fun getMangaDetails(mangaId: String): Manga
suspend fun searchManga(query: String, offset: Int = 0, limit: Int = 20): List<Manga>

// ChapterRepository
suspend fun getChapterList(
    mangaId: String,
    limit: Int = 20,
    offset: Int = 0,
    language: MangaLanguage = MangaLanguage.ENGLISH,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
): List<Chapter>
suspend fun getChapterDetails(chapterId: String): Chapter
suspend fun getChapterPages(chapterId: String, mangaId: String): ChapterPages

// CacheRepository
suspend fun addChapterCache(chapterPages: ChapterPages)
suspend fun getChapterCache(chapterId: String): ChapterPages
suspend fun deleteChapterCache(chapterId: String)
suspend fun clearExpiredCache(olderThan: Long)

// CategoryRepository
suspend fun getCategoryList(): List<Category>
suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int = 0,
    sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
    statusFilter: List<MangaStatus> = listOf(MangaStatus.ON_GOING),
    contentRatingFilter: List<MangaContentRating> = listOf(MangaContentRating.SAFE),
): List<Manga>

// FavoritesRepository
fun observeFavorites(userId: String, limit: Int = 20, lastFavoriteMangaId: String? = null): Flow<List<FavoriteManga>>
suspend fun addToFavorites(userId: String, manga: FavoriteManga)
suspend fun removeFromFavorites(userId: String, mangaId: String)
fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>

// HistoryRepository
fun observeHistory(userId: String, limit: Int = 10, mangaId: String? = null, lastReadingHistoryId: String? = null): Flow<List<ReadingHistory>>
suspend fun upsertHistory(userId: String, readingHistory: ReadingHistory)
suspend fun removeFromHistory(userId: String, readingHistoryId: String)

// UserRepository
suspend fun register(email: String, password: String, name: String)
suspend fun login(email: String, password: String)
suspend fun logout()
suspend fun sendResetPassword(email: String)
fun observeCurrentUser(): Flow<User?>
suspend fun updateUserProfile(user: User)
fun observeUserProfile(userId: String): Flow<User?>

// SettingsRepository
fun observeThemeMode(): Flow<ThemeMode>
suspend fun saveThemeMode(value: ThemeMode)
```

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

**With validation** — use cases call `User.validate*()` before the repository call. Validation exceptions propagate as-is inside `runSuspendResultCatching` and become `Result.failure`.

---

## Use Case Reference

All 32 use cases. One-shot = `suspend operator fun invoke`; Reactive = `operator fun invoke` returning `Flow`.

### manga/ (10)

| Use Case | Signature | Notes |
|---|---|---|
| `GetLatestUpdateMangaListUseCase` | `suspend invoke(): Result<List<Manga>>` | |
| `GetTrendingMangaListUseCase` | `suspend invoke(): Result<List<Manga>>` | |
| `GetNewReleaseMangaListUseCase` | `suspend invoke(): Result<List<Manga>>` | |
| `GetTopRatedMangaListUseCase` | `suspend invoke(): Result<List<Manga>>` | |
| `GetMangaDetailsUseCase` | `suspend invoke(mangaId: String): Result<Manga>` | |
| `GetChapterListUseCase` | `suspend invoke(mangaId, limit=20, offset=0, language=ENGLISH, sortOrder=DESC): Result<List<Chapter>>` | |
| `GetChapterDetailsUseCase` | `suspend invoke(chapterId: String): Result<Chapter>` | |
| `GetChapterPagesUseCase` | `suspend invoke(chapterId: String, mangaId: String): Result<ChapterPages>` | |
| `SearchMangaUseCase` | `suspend invoke(query: String, offset=0, limit=20): Result<List<Manga>>` | |
| `GetMangaSuggestionsUseCase` | `suspend invoke(query: String): Result<List<String>>` | private `SUGGESTION_LIMIT = 10` |

### manga/cache/ (4)

| Use Case | Signature | Notes |
|---|---|---|
| `AddChapterCacheUseCase` | `suspend invoke(chapterPages: ChapterPages): Result<Unit>` | |
| `GetChapterCacheUseCase` | `suspend invoke(chapterId: String): Result<ChapterPages>` | cache miss → `Result.failure` |
| `DeleteChapterCacheUseCase` | `suspend invoke(chapterId: String): Result<Unit>` | |
| `ClearExpiredCacheUseCase` | `suspend invoke(): Result<Unit>` | private `CACHE_EXPIRY_MILLIS = 24h` |

### category/ (2)

| Use Case | Signature | Notes |
|---|---|---|
| `GetCategoryListUseCase` | `suspend invoke(): Result<Map<CategoryType, List<Category>>>` | groupBy logic lives here, not in VM |
| `GetMangaListByCategoryUseCase` | `suspend invoke(categoryId, offset=0, sortCriteria=LATEST_UPDATE, sortOrder=DESC, statusFilter=[ON_GOING], contentRatingFilter=[SAFE]): Result<List<Manga>>` | |

### settings/ (2)

| Use Case | Signature | Notes |
|---|---|---|
| `ObserveThemeModeUseCase` | `invoke(): Flow<Result<ThemeMode>>` | Reactive |
| `SaveThemeModeUseCase` | `suspend invoke(value: ThemeMode): Result<Unit>` | |

### user/auth/ (4)

| Use Case | Signature | Notes |
|---|---|---|
| `LoginUseCase` | `suspend invoke(email: String, password: String): Result<Unit>` | validates email + password |
| `RegisterUseCase` | `suspend invoke(email: String, password: String, confirmPassword: String, name: String): Result<Unit>` | validates all 4 fields |
| `LogoutUseCase` | `suspend invoke(): Result<Unit>` | |
| `SendResetPasswordUseCase` | `suspend invoke(email: String): Result<Unit>` | validates email |

### user/profile/ (3)

| Use Case | Signature | Notes |
|---|---|---|
| `ObserveCurrentUserUseCase` | `invoke(): Flow<Result<User?>>` | Reactive |
| `ObserveUserProfileUseCase` | `invoke(userId: String): Flow<Result<User?>>` | Reactive |
| `UpdateUserProfileUseCase` | `suspend invoke(currentUser: User, newName: String?, newAvatarUrl: String?): Result<Unit>` | validates name if changed; early return if nothing changed |

### user/favorites/ (4)

| Use Case | Signature | Notes |
|---|---|---|
| `ObserveFavoritesUseCase` | `invoke(userId: String, limit=20, lastFavoriteMangaId=null): Flow<Result<List<FavoriteManga>>>` | Reactive; cursor-based pagination |
| `ObserveIsFavoriteUseCase` | `invoke(userId: String, mangaId: String): Flow<Result<Boolean>>` | Reactive |
| `AddToFavoritesUseCase` | `suspend invoke(userId: String, manga: Manga): Result<Unit>` | **Only use case that constructs a domain entity from another** — `Manga → FavoriteManga` (business logic, not mapping) |
| `RemoveFromFavoritesUseCase` | `suspend invoke(userId: String, mangaId: String): Result<Unit>` | |

### user/history/ (3)

| Use Case | Signature | Notes |
|---|---|---|
| `ObserveHistoryUseCase` | `invoke(userId: String, limit=10, mangaId=null, lastReadingHistoryId=null): Flow<Result<List<ReadingHistory>>>` | Reactive; default limit is **10**, not 20 |
| `UpsertHistoryUseCase` | `suspend invoke(userId, mangaId, mangaTitle, mangaCoverUrl, chapterId, chapterTitle, chapterNumber, chapterVolume, lastReadPage, pageCount): Result<Unit>` | takes flat primitives — VM must not construct `ReadingHistory` directly; use case builds it internally via `ReadingHistory.generateId(...)` |
| `RemoveFromHistoryUseCase` | `suspend invoke(userId: String, readingHistoryId: String): Result<Unit>` | |
