# DexReader — Refactoring Progress

## Phase Overview

| #   | Phase                                                                          | Status  |
| --- | ------------------------------------------------------------------------------ | ------- |
| 1   | Domain Enums — replace raw strings with typed enums end-to-end                 | ✅ done |
| 2   | Domain Model Property Renames — domain-meaningful names                        | ✅ done |
| 3   | Domain Model Default Constants — companion object fallbacks                    | ✅ done |
| 4   | Mapper Object Pattern — wrap all data mappers in `object`                      | ✅ done |
| 5   | CategoryGroup → CategoryType — sealed class → domain enum                      | ✅ done |
| 6   | CA Fix: UseCase Grouping — move client-side transforms out of VMs (categories) | ✅ done |
| 7   | Business Logic Extraction — all remaining CA gaps in ViewModels                | ✅ done |
| 8   | CA Audit + Bug Fix — full VM review, CancellationException swallowing fixed    | ✅ done |
| 9   | Domain Exception Refactoring + Mapper Extraction                               | ✅ done |

---

## Phase Detail

### Phase 1 — Domain Enums ✅

All 5 domain enums wired end-to-end across all three layers:

| Domain enum                | Presentation Option enum         | Mapper           |
| -------------------------- | -------------------------------- | ---------------- |
| `MangaLanguage`            | `MangaLanguageName` (@StringRes) | `LanguageMapper` |
| `MangaSortCriteria`        | `MangaSortCriteriaOption`        | `CriteriaMapper` |
| `MangaSortOrder`           | `MangaSortOrderOption`           | `CriteriaMapper` |
| `MangaStatusFilter`        | `MangaStatusFilterOption`        | `CriteriaMapper` |
| `MangaContentRatingFilter` | `MangaContentRatingFilterOption` | `CriteriaMapper` |

- ISO/API strings live only in `data/` (`ApiParamMapper`, `ChapterMapper`)
- `@StringRes` lives only in `presentation/model/`
- Mapper objects in `presentation/mapper/` use `valueOf(this.name)` — enum name identity across
  layers
- `rememberSaveable` for enum state: `Saver(save = { it.name }, restore = { Enum.valueOf(it) })`

### Phase 2 — Domain Model Property Renames ✅

| Model            | Old → New                                                                                                           |
| ---------------- | ------------------------------------------------------------------------------------------------------------------- |
| `Chapter`        | `publishAt` → `publishedAt`, `translatedLanguage` → `language`, `chapterNumber` → `number`                          |
| `Manga`          | `availableTranslatedLanguages` → `availableLanguages`, `lastUpdated` → `updatedAt`, `lastChapter` → `latestChapter` |
| `ChapterPages`   | `chapterDataHash` → `dataHash`, `pageUrls` → `pages`                                                                |
| `Category`       | `group` → `type`                                                                                                    |
| `ReadingHistory` | `totalChapterPages` → `pageCount`                                                                                   |
| `User`           | `profilePictureUrl` → `avatarUrl`                                                                                   |

- `ChapterCacheEntity.chapterDataHash` → Kotlin property `dataHash`;
  `@ColumnInfo(name = "chapterDataHash")` kept for Room DB column compat
- Firebase DTOs kept as-is — mapper is the boundary

### Phase 3 — Domain Model Default Constants ✅

All domain models own fallback values as `companion object` constants:

- `Manga` — `DEFAULT_TITLE/DESCRIPTION/AUTHOR/ARTIST/STATUS/YEAR/LAST_CHAPTER/LAST_UPDATED`
- `Chapter` — `DEFAULT_MANGA_ID/TITLE/CHAPTER_NUMBER/VOLUME/LANGUAGE`
- `Category` — `DEFAULT_TITLE`, `DEFAULT_TYPE` (= `CategoryType.UNKNOWN`)
- `ChapterPages` — `DEFAULT_BASE_URL/HASH`
- `FavoriteManga` — `DEFAULT_ADDED_AT`
- `User` — `DEFAULT_NAME/EMAIL`

### Phase 4 — Mapper Object Pattern ✅

All data layer mappers wrapped in `object`:
`CategoryMapper`, `ChapterMapper`, `ChapterPagesMapper`, `MangaMapper`, `ApiParamMapper`,
`FavoriteMangaMapper`, `ReadingHistoryMapper`, `UserMapper`, `ExceptionMapper`

Call sites use qualified static import: `import ObjectName.functionName`

### Phase 5 — CategoryGroup → CategoryType ✅

- `CategoryGroup` sealed class deleted
- `domain/model/CategoryType.kt` — `GENRE, THEME, FORMAT, CONTENT, UNKNOWN`
- `presentation/model/CategoryTypeOption.kt` — `@StringRes val nameRes: Int`
- `presentation/mapper/CategoryTypeMapper.kt` — `CategoryType.toCategoryTypeOption()` (domain →
  presentation only)
- `Category.type: String` → `Category.type: CategoryType`
- `CategoryMapper` — private `String.toCategoryType()` via case-insensitive `entries.firstOrNull`
- Components renamed: `CategoryGroupSection` → `CategoryTypeSection`, `CategoryGroupHeader` →
  `CategoryTypeHeader`
- 5 string resources added

### Phase 6 — CA Fix: UseCase Grouping ✅ (categories screen)

**Problem:** `CategoriesViewModel` was doing
`categoryList.filter { it.type == CategoryType.GENRE }` × 4 — client-side filter belongs in UseCase
per domain layer guidelines.

**Fix:**

- `GetCategoryListUseCase` now returns `Result<Map<CategoryType, List<Category>>>` with
  `groupBy { it.type }` inside
- `CategoriesUiState.Success`: 4 named lists → `Map<CategoryTypeOption, List<Category>> categoryMap`
- `CategoriesViewModel`: converts domain map → presentation map using
  `CategoryTypeOption.entries.associateWith { option -> grouped[CategoryType.valueOf(option.name)] ?: emptyList() }`
- `CategoriesContent`: 4 hardcoded `item {}` → single dynamic `items(categoryMap.keys.toList())`

### Phase 7 — Business Logic Extraction ✅

All 8 identified CA gaps resolved. Grouped by sub-phase:

#### Phase 6 extension — MangaDetailsViewModel (Gaps #2 + #5)

- **Gap #2:** `AddToFavoritesUseCase` accepts `Manga`, builds `FavoriteManga` internally; VM no
  longer imports `FavoriteManga`
- **Gap #5:** `ReadingHistory.findContinueTarget(historyList)` companion method; VM delegates to it

#### Gaps #3 + #6 — ReaderViewModel domain helpers

- **Gap #3:** `ReadingHistory.generateId(mangaId, chapterId)` companion method; VM uses it
- **Gap #6:** `ReadingHistory.findInitialPage(chapterId, navChapterId, navPage, historyList)`
  companion method; `getInitialChapterPage()` private VM method deleted

#### Gap #4 — ClearExpiredCacheUseCase autonomy

- `invoke()` takes no params; owns `CACHE_EXPIRY_MILLIS = 24h` internally
- VM constant `CACHE_EXPIRY_TIME_MILLIS` removed

#### Gap #8 — HomeViewModel import fix

- `jakarta.inject.Inject` → `javax.inject.Inject`

#### Gap #7 — FavoritesHistoryException.PermissionDenied

- New `PermissionDenied` subtype in `domain/exception/FavoritesHistoryException.kt`
- `FavoritesRepositoryImpl` + `HistoryRepositoryImpl`: `.catch` operator maps
  `FirebaseFirestoreException(PERMISSION_DENIED)` at the repo boundary
- All 4 VMs: `throwable.message?.contains(PERMISSION_DENIED_EXCEPTION)` →
  `throwable is FavoritesHistoryException.PermissionDenied`; constant removed

#### Phase 7 Item A — ReadingHistory construction out of VMs

- `AddAndUpdateToHistoryUseCase.invoke()` accepts 10 raw fields; constructs `ReadingHistory`
  internally
- `ReaderViewModel` passes raw fields

#### Phase 7 Item B — BaseNextPageState.fromPageSize()

- `companion object { fun fromPageSize(resultSize: Int, pageSize: Int): BaseNextPageState }` added
- All 6 VMs updated: `CategoryDetailsViewModel`, `SearchViewModel`, `MangaDetailsViewModel`,
  `FavoritesViewModel`, `HistoryViewModel`, `ReaderViewModel`
- `ReaderViewModel.hasNextChapterListPage` Boolean field deliberately NOT converted (different
  pagination model)

#### Phase 7 Item C — GetMangaSuggestionsUseCase

- New use case: `repository.searchManga(query, offset=0).take(SUGGESTION_LIMIT).map { it.title }`;
  owns `SUGGESTION_LIMIT = 10`
- `SearchViewModel` injects it; `TAKE_SUGGESTION_LIST_SIZE` constant removed

#### Phase 7 Item D — Chapter.shouldPrefetchNextPage() / isPrefetchNextPage()

- `Chapter.isPrefetchNextPage(currentIndex, listSize): Boolean` with `PREFETCH_THRESHOLD = 5` in
  companion
- `ReaderViewModel.NEARED_LAST_CHAPTER_COUNT` removed; delegates to domain method

#### Phase 7 Item E — Chapter.NavPosition + determineNavPosition()

- `Chapter.NavPosition` nested data class added: `foundAtIndex`, `previousChapterId`,
  `nextChapterId`, `canNavigatePrevious`, `canNavigateNext`
- `Chapter.determineNavPosition(currentChapterId, chapterList, hasNextPage): NavPosition` companion
  method added — pure list-index computation, zero Android dependencies
- `ReaderViewModel.updateChapterNavState()` body replaced: delegates to
  `Chapter.determineNavPosition()`, passes `nav.foundAtIndex` directly to
  `prefetchChapterListNextPage()`
- All inline `indexOfFirst`, `isFirstChapter`, `isLastChapter` locals eliminated from VM

#### Phase 7 Item F — observeIsFetchDataDone() combine (kept as-is)

- Evaluated:
  `combine(_isFetchChapterDetailsDone, _isFetchMangaDetailsDone, _isObserveHistoryDone) { a, b, c -> a && b && c }`
  is coroutine/flow **orchestration** — a loading gate waiting for 3 parallel async operations
- No domain business rule here; `delay() + fetchChapterPages()` is VM-level side-effect coordination
- **Decision:** kept exactly as-is in `ReaderViewModel`

---

### Phase 8 — CA Audit + CancellationException Bug Fix ✅

#### Full VM Audit

Reviewed all 14 ViewModels against `DOMAIN_LAYER_GUIDELINES.md`. 12 VMs confirmed clean. One
structural bug found.

**Audit result — all VMs clean except:**

| VM                   | Finding                                                             |
| -------------------- | ------------------------------------------------------------------- |
| `FavoritesViewModel` | `catch (e: Exception)` swallows `CancellationException` in 2 places |
| `HistoryViewModel`   | Same pattern in 2 places                                            |

#### CancellationException Fix

**Root cause:** `toFlowResult()` correctly rethrows `CancellationException` (does not wrap it as
`Result.failure`). However, the rethrown exception still propagates through `collect { }` and
reaches the outer `try-catch (e: Exception)` in both VMs.

**Fix applied:** Added `catch (c: CancellationException) { throw c }` before each
`catch (e: Exception)` block, plus `import kotlin.coroutines.cancellation.CancellationException` in
both files.

---

### Phase 9 — Domain Exception Refactoring + Mapper Extraction ✅

#### 9A — Exception Hierarchy Restructuring

**Problem:** Exceptions grouped by infrastructure (`RemoteException`, `CacheException`) rather than
business domain. Leaked infrastructure details into domain layer.

**Fix:**

- Added top-level `DomainException` subtypes: `NetworkUnavailable`, `ServiceRequestFailed`, `Unknown`
- Added `MangaException.ChapterDataNotFound` (replaces `CacheException.NotFound`)
- Split `FavoritesHistoryException` → `FavoritesException` + `HistoryException`
- Deleted: `RemoteException.kt`, `CacheException.kt`, `FavoritesHistoryException.kt`
- Changed all leaf exceptions from `data class` to `class` (no structural equality needed)

#### 9B — Repository Exception Mapping

- All 7 repo impls updated with hermetic domain boundary
- `else → throw e` replaced with `else → throw DomainException.Unknown(cause = e)` everywhere
- Centralized mapping helpers introduced per repo

#### 9C — Extension Function Conversion

- Private `mapApiException(e: Exception)` / `mapFirestoreException(e: Exception)` converted to
  `Exception.mapToDomainException()` extension functions using `this` instead of `e`

#### 9D — Extraction to `ExceptionMapper.kt`

- Created `data/mapper/ExceptionMapper.kt` — `object` with 4 extension functions:
  - `Exception.toApiDomainException()` — `HttpException`, `IOException` → `DomainException`
  - `Exception.toFavoritesDomainException()` — `FirebaseFirestoreException` → `FavoritesException`
  - `Exception.toHistoryDomainException()` — `FirebaseFirestoreException` → `HistoryException`
  - `Exception.toCacheDomainException()` — `DomainException` passthrough, else `Unknown`
- All private mapper functions removed from 6 repo impls
- Unused imports cleaned up

#### 9E — Presentation Layer Updates

- `FavoritesViewModel`: `FavoritesHistoryException` → `FavoritesException`
- `HistoryViewModel`: `FavoritesHistoryException` → `HistoryException`
- `MangaDetailsViewModel`: Split into `FavoritesException` / `HistoryException` by context
- `ReaderViewModel`: `FavoritesHistoryException` → `HistoryException`

---

## Architecture Decisions Log

| Decision                                                                     | Rationale                                                                                                                                                          |
| ---------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Domain enums have zero Android/framework imports                             | Domain layer must be pure Kotlin                                                                                                                                   |
| Presentation Option enums hold `@StringRes`                                  | Only presentation layer can reference Android resources                                                                                                            |
| Mapper objects use `valueOf(this.name)`                                      | Enum entry names are kept identical across layers — no lookup table needed                                                                                         |
| `toCategoryType()` mapper NOT created                                        | Inverse direction (presentation → domain) not needed for category type; inline `CategoryType.valueOf(option.name)` used in ViewModel                               |
| `GetCategoryListUseCase` returns grouped map                                 | Client-side `filter {}` belongs in UseCase per domain guidelines                                                                                                   |
| `CategoriesUiState.Success` uses `Map<CategoryTypeOption, List<Category>>`   | Presentation map key = presentation type; ViewModel is the domain→presentation boundary                                                                            |
| `CategoriesContent` iterates map keys dynamically                            | Decoupled from hardcoded 4-section assumption; if API drops a type, no crash                                                                                       |
| `ChapterCacheEntity` column name kept as `chapterDataHash`                   | Changing Room column name requires a DB migration; Kotlin property renamed, `@ColumnInfo` preserved                                                                |
| Firebase DTOs not renamed                                                    | They're at the network boundary; mapper is the adapter                                                                                                             |
| Exception mapping at repository boundary                                     | VM should only see domain exceptions, not Firebase/Retrofit internals                                                                                              |
| `.catch` before `toFlowResult()`                                             | Exception remapping must happen on the raw Flow before wrapping in `Result`                                                                                        |
| `GetMangaSuggestionsUseCase` uses `.take()` not `limit` param                | `MangaRepository.searchManga` has no `limit` parameter; `.take()` is idiomatic Kotlin                                                                              |
| `ReaderViewModel.hasNextChapterListPage` Boolean not migrated                | It drives manual job orchestration, not `BasePaginationUiState`-based pagination                                                                                   |
| `BaseNextPageState.fromPageSize()` is single source of truth                 | Eliminates inline `if (size >= pageSize) IDLE else NO_MORE_ITEMS` duplicated across 6 VMs                                                                          |
| Domain model companion methods for pure rules                                | `ReadingHistory.generateId/findContinueTarget/findInitialPage`, `Chapter.isPrefetchNextPage/determineNavPosition` — pure functions belong in domain model, not VMs |
| `observeIsFetchDataDone()` kept in VM                                        | `combine` of 3 loading flags is coroutine orchestration (loading gate), not a domain rule                                                                          |
| `catch (c: CancellationException) { throw c }` before `catch (e: Exception)` | `toFlowResult()` rethrows `CancellationException` — must be rethrown to preserve structured concurrency                                                            |
| Exceptions grouped by business domain, not infrastructure                    | `MangaException`, `FavoritesException`, `HistoryException` etc. — not `RemoteException`, `CacheException`                                                          |
| Hermetic domain boundary via `DomainException.Unknown`                       | All unmapped framework exceptions wrapped — no infrastructure details leak into domain/presentation                                                                |
| `FavoritesHistoryException` split into two                                   | Favorites and History are distinct features that should not share exception types                                                                                  |
| Leaf exceptions as `class` not `data class`                                  | Structural equality for exceptions is rarely needed; reduces boilerplate                                                                                           |
| `ExceptionMapper` centralized in `data/mapper/`                              | Follows project's `object` + extension function mapper pattern; eliminates duplicate private mapper functions across 6 repos                                       |
