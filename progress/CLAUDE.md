# DexReader — Claude Session Handoff

## Status

**Current phase:** Phase 9 — Domain Exception Refactoring + Mapper Extraction (complete)
**Overall progress:** All 9 phases complete. Domain exceptions restructured, exception mappers
centralized in `data/mapper/ExceptionMapper.kt`, all repos cleaned.

---

## Completed This Session

### Phase 9A — Domain Exception Hierarchy Refactoring

- **`DomainException.kt`**: Added `NetworkUnavailable`, `ServiceRequestFailed`, `Unknown` as
  top-level subtypes with `message` parameter on base class
- **`MangaException.kt`**: Added `ChapterDataNotFound`, changed all leaf exceptions from
  `data class` to `class`
- **`UserException.kt`**: Changed all leaf exceptions from `data class` to `class`
- **`FavoritesException.kt` [NEW]**: Split from `FavoritesHistoryException` — contains
  `PermissionDenied`
- **`HistoryException.kt` [NEW]**: Split from `FavoritesHistoryException` — contains
  `PermissionDenied`
- **Deleted**: `RemoteException.kt`, `CacheException.kt`, `FavoritesHistoryException.kt`

### Phase 9B — Repository Exception Mapping

- All 7 repo impls updated to map infrastructure exceptions to domain exceptions
- `CacheRepositoryImpl`: `CacheException.NotFound` → `MangaException.ChapterDataNotFound`,
  all `else` → `DomainException.Unknown`
- `MangaRepositoryImpl`, `CategoryRepositoryImpl`, `ChapterRepositoryImpl`: centralized API
  exception mapping (`HttpException` → `ServiceRequestFailed`, `IOException` → `NetworkUnavailable`)
- `FavoritesRepositoryImpl`, `HistoryRepositoryImpl`: centralized Firestore exception mapping
- `UserRepositoryImpl`: hermetic boundary added (`else → DomainException.Unknown`)

### Phase 9C — Mapper Functions → Extension Functions

- Converted all `mapApiException(e)` / `mapFirestoreException(e)` private functions to
  `Exception.mapToDomainException()` extension functions in each repo impl

### Phase 9D — Extract Exception Mappers to `ExceptionMapper.kt`

- Created `data/mapper/ExceptionMapper.kt` with 4 extension functions following project's
  `object` + extension function pattern:
  - `Exception.toApiDomainException()` — used by Manga, Category, Chapter repos
  - `Exception.toFavoritesDomainException()` — used by Favorites repo
  - `Exception.toHistoryDomainException()` — used by History repo
  - `Exception.toCacheDomainException()` — used by Cache repo
- Removed all private mapper functions from 6 repo impls
- Cleaned up unused imports (`DomainException`, `HttpException`, `IOException`, `retrofit2`)

### Phase 9E — Presentation Layer Updates

- `FavoritesViewModel`: `FavoritesHistoryException` → `FavoritesException`
- `HistoryViewModel`: `FavoritesHistoryException` → `HistoryException`
- `MangaDetailsViewModel`: Split into `FavoritesException` and `HistoryException` by context
- `ReaderViewModel`: `FavoritesHistoryException` → `HistoryException`

### Misc

- `AsyncHandler.kt`: Added explicit `CancellationException` rethrowing in `runSuspendResultCatching`
  and `toFlowResult` to preserve structured concurrency

---

## Next Session — Start Here

No deferred items remain. All 9 phases complete. Possible next steps:

- **Unit tests** for exception mapping in repos and ViewModels (currently no test coverage)
- Feature development as needed
- The user renamed `ParamMapper` → `ApiParamMapper` (their own change outside this session's scope)

---

## Important Context

- **Exception naming by business domain**: Exceptions grouped by feature (`MangaException`,
  `UserException`, `FavoritesException`, `HistoryException`), not infrastructure topology
- **Hermetic domain boundary**: All repos wrap unknown exceptions in `DomainException.Unknown` —
  no infrastructure exceptions leak into domain/presentation layers
- **`ExceptionMapper` follows project pattern**: `object` with extension functions, imported
  via `import ExceptionMapper.toXxxDomainException`
- **`data class` → `class` for leaf exceptions**: Structural equality for exceptions is rarely
  needed; reduces boilerplate
- **`FavoritesHistoryException` fully removed**: Split into `FavoritesException` and
  `HistoryException` to decouple features
- **`ParamMapper` renamed to `ApiParamMapper`**: Done by user outside this session

---

## Files Modified This Session

| File                                                          | Change                                                        |
| ------------------------------------------------------------- | ------------------------------------------------------------- |
| `domain/exception/DomainException.kt`                         | Added `NetworkUnavailable`, `ServiceRequestFailed`, `Unknown` |
| `domain/exception/MangaException.kt`                          | Added `ChapterDataNotFound`, `data class` → `class`           |
| `domain/exception/UserException.kt`                           | `data class` → `class`                                        |
| `domain/exception/FavoritesException.kt`                      | **[NEW]** split from `FavoritesHistoryException`              |
| `domain/exception/HistoryException.kt`                        | **[NEW]** split from `FavoritesHistoryException`              |
| `domain/exception/RemoteException.kt`                         | **[DELETED]**                                                 |
| `domain/exception/CacheException.kt`                          | **[DELETED]**                                                 |
| `domain/exception/FavoritesHistoryException.kt`               | **[DELETED]**                                                 |
| `data/mapper/ExceptionMapper.kt`                              | **[NEW]** centralized exception mapping                       |
| `data/repository/MangaRepositoryImpl.kt`                      | Uses `toApiDomainException()`                                 |
| `data/repository/CategoryRepositoryImpl.kt`                   | Uses `toApiDomainException()`                                 |
| `data/repository/ChapterRepositoryImpl.kt`                    | Uses `toApiDomainException()`                                 |
| `data/repository/CacheRepositoryImpl.kt`                      | Uses `toCacheDomainException()`                               |
| `data/repository/FavoritesRepositoryImpl.kt`                  | Uses `toFavoritesDomainException()`                           |
| `data/repository/HistoryRepositoryImpl.kt`                    | Uses `toHistoryDomainException()`                             |
| `data/repository/UserRepositoryImpl.kt`                       | Hermetic boundary added                                       |
| `presentation/screens/favorites/FavoritesViewModel.kt`        | `FavoritesException`                                          |
| `presentation/screens/history/HistoryViewModel.kt`            | `HistoryException`                                            |
| `presentation/screens/manga_details/MangaDetailsViewModel.kt` | Split exceptions                                              |
| `presentation/screens/reader/ReaderViewModel.kt`              | `HistoryException`                                            |
| `util/AsyncHandler.kt`                                        | `CancellationException` rethrowing                            |
