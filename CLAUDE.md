# DexReader — Claude Session Handoff

## Status
**Current phase:** Business Logic Extraction — all known ViewModel CA gaps resolved
**Overall progress:** All enum/rename/mapper/grouping phases complete; all 8 identified CA gaps done; Phase 7 A–D complete; minor remaining items in `ReaderViewModel` (E, F) deferred to next session

---

## Completed This Session

### Phase 6 — MangaDetailsViewModel (Gaps #2 + #5)
- **Gap #2:** `AddToFavoritesUseCase` now accepts `Manga` instead of `FavoriteManga`; constructs `FavoriteManga` internally — VM no longer imports `FavoriteManga`
- **Gap #5:** `ReadingHistory.findContinueTarget(historyList)` added to companion object; `MangaDetailsViewModel.continueChapter` StateFlow delegates to it

### Gaps #3 + #6 — ReaderViewModel domain helpers
- **Gap #3:** `ReadingHistory.generateId(mangaId, chapterId)` companion method added; `ReaderViewModel` uses it
- **Gap #6:** `ReadingHistory.findInitialPage(chapterId, navChapterId, navPage, historyList)` companion method added; `getInitialChapterPage()` private VM method deleted

### Gap #4 — ClearExpiredCacheUseCase
- `ClearExpiredCacheUseCase.invoke()` now takes no parameters; owns `CACHE_EXPIRY_MILLIS = 24 * 60 * 60 * 1000L` internally
- `ReaderViewModel` call site updated (no-arg), `CACHE_EXPIRY_TIME_MILLIS` constant removed from VM

### Gap #8 — HomeViewModel import fix
- `import jakarta.inject.Inject` → `import javax.inject.Inject`

### Gap #7 — FavoritesHistoryException.PermissionDenied (4 VMs)
- Added `PermissionDenied` subtype to `domain/exception/FavoritesHistoryException.kt`
- `FavoritesRepositoryImpl`: `.catch` operator added to `observeIsFavorite()` and `observeFavorites()` — maps `FirebaseFirestoreException(PERMISSION_DENIED)` → `FavoritesHistoryException.PermissionDenied`
- `HistoryRepositoryImpl`: same `.catch` mapping added to `observeHistory()`
- All 4 VMs (`MangaDetailsViewModel`, `ReaderViewModel`, `FavoritesViewModel`, `HistoryViewModel`): string-match `throwable.message?.contains(PERMISSION_DENIED_EXCEPTION)` replaced with type check `throwable is FavoritesHistoryException.PermissionDenied`; `PERMISSION_DENIED_EXCEPTION` constant removed from all VMs

### Phase 7 — Business Logic Extraction (A–D)

**A — ReadingHistory construction out of ReaderViewModel**
- `AddAndUpdateToHistoryUseCase.invoke()` now accepts 10 raw fields and constructs `ReadingHistory` internally using `ReadingHistory.generateId()`
- `ReaderViewModel` passes raw fields; no longer constructs `ReadingHistory` directly

**B — BaseNextPageState.fromPageSize() companion factory**
- `BaseNextPageState` gains `companion object { fun fromPageSize(resultSize: Int, pageSize: Int): BaseNextPageState }`
- All 6 VMs updated: `CategoryDetailsViewModel`, `SearchViewModel`, `MangaDetailsViewModel`, `FavoritesViewModel`, `HistoryViewModel`, `ReaderViewModel` — inline `if (list.size >= PAGE_SIZE) IDLE else NO_MORE_ITEMS` replaced with `BaseNextPageState.fromPageSize(list.size, PAGE_SIZE)`
- **Note:** `ReaderViewModel.hasNextChapterListPage` is a plain `Boolean` field (not StateFlow-based pagination) — deliberately NOT converted

**C — GetMangaSuggestionsUseCase (new)**
- New `domain/usecase/manga/GetMangaSuggestionsUseCase.kt`: calls `repository.searchManga(query, offset=0).take(SUGGESTION_LIMIT).map { it.title }`; owns `SUGGESTION_LIMIT = 10`
- `SearchViewModel` now injects and delegates to this use case; `TAKE_SUGGESTION_LIST_SIZE` constant removed; no longer calls `searchMangaUseCase` for suggestions

**D — Chapter.shouldPrefetchNextPage() domain helper**
- `Chapter.kt` companion object gains `shouldPrefetchNextPage(currentIndex, listSize): Boolean` with `PREFETCH_THRESHOLD = 5`
- `ReaderViewModel.prefetchChapterListNextPage()` call site delegates to it; `NEARED_LAST_CHAPTER_COUNT` constant removed

---

## Next Session — Start Here

1. Open `memory/refactoring-gaps.md` — two items remain (E and F) in `ReaderViewModel`
2. **Item E:** `updateChapterNavState()` — chapter navigation state logic lives inside VM; move rule to domain model or use case
3. **Item F:** `combine` of 3 boolean flags — data coordination logic in `ReaderViewModel`; evaluate if it belongs in a use case
4. After E + F: check `Phase 8` (MangaLanguageCodeParam cleanup in `ParamMapper`/`CategoryMapper`)

---

## Important Context

- **Exception mapping boundary:** Firebase `FirebaseFirestoreException(PERMISSION_DENIED)` is now caught at the **repository layer** (`.catch` on the Flow) and remapped to `FavoritesHistoryException.PermissionDenied` before reaching any VM — VMs only do type checks, never string matching
- **`toFlowResult()` in `AsyncHandler`:** wraps Flow exceptions as `Result.failure` — the `.catch` must be applied upstream of `toFlowResult()` for the mapping to work
- **`ReadingHistory.generateId()`** is called both in `AddAndUpdateToHistoryUseCase` and retained in `ReaderViewModel` for ID lookup (read path) — this is intentional
- **`GetMangaSuggestionsUseCase`** uses `.take()` instead of a `limit` param because `MangaRepository.searchManga` has no `limit` parameter — rule still moves out of VM
- **`ReaderViewModel.hasNextChapterListPage`** is a stateful `Boolean` field driving manual job orchestration, not a `BasePaginationUiState`-based list — `fromPageSize()` deliberately not applied there
- **`BaseNextPageState.fromPageSize()`** is the single source of truth for the "has more pages" heuristic — `resultSize >= pageSize` → `IDLE`, else `NO_MORE_ITEMS`
- **Enum name identity rule** — all three-layer enums share identical entry names, enabling `valueOf(name)` without a lookup table (established in earlier phases)

---

## Files Modified This Session

| File | Change |
|---|---|
| `domain/exception/FavoritesHistoryException.kt` | Added `PermissionDenied` subtype |
| `domain/model/ReadingHistory.kt` | Added `generateId()`, `findContinueTarget()`, `findInitialPage()` to companion |
| `domain/model/Chapter.kt` | Added `shouldPrefetchNextPage()` + `PREFETCH_THRESHOLD` to companion |
| `domain/usecase/favorites/AddToFavoritesUseCase.kt` | Param `manga: FavoriteManga` → `manga: Manga`; constructs `FavoriteManga` internally |
| `domain/usecase/history/AddAndUpdateToHistoryUseCase.kt` | Accepts 10 raw fields; constructs `ReadingHistory` internally |
| `domain/usecase/cache/ClearExpiredCacheUseCase.kt` | `invoke()` takes no params; owns `CACHE_EXPIRY_MILLIS` internally |
| `domain/usecase/manga/GetMangaSuggestionsUseCase.kt` | **new** — extracts suggestion fetch + take/map from `SearchViewModel` |
| `data/repository/FavoritesRepositoryImpl.kt` | `.catch` mapping added to `observeIsFavorite()` + `observeFavorites()` |
| `data/repository/HistoryRepositoryImpl.kt` | `.catch` mapping added to `observeHistory()` |
| `presentation/screens/common/base/BaseNextPageState.kt` | Added `companion object { fun fromPageSize(...) }` |
| `presentation/screens/manga_details/MangaDetailsViewModel.kt` | Removed `FavoriteManga` import/construction; delegates `continueChapter` + PERMISSION_DENIED; uses `fromPageSize()` |
| `presentation/screens/reader/ReaderViewModel.kt` | Delegates `findInitialPage`, `shouldPrefetchNextPage`, `clearExpiredCacheUseCase()`; removes `ReadingHistory` construction + PERMISSION_DENIED + constants |
| `presentation/screens/search/SearchViewModel.kt` | Injects `GetMangaSuggestionsUseCase`; uses `fromPageSize()` |
| `presentation/screens/category_details/CategoryDetailsViewModel.kt` | Uses `fromPageSize()` |
| `presentation/screens/favorites/FavoritesViewModel.kt` | PERMISSION_DENIED type check; uses `fromPageSize()` |
| `presentation/screens/history/HistoryViewModel.kt` | PERMISSION_DENIED type check; uses `fromPageSize()` |
| `presentation/screens/home/HomeViewModel.kt` | `jakarta.inject` → `javax.inject` |
