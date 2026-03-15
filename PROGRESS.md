# Project Progress Tracker

## Completed Phases

- [x] **Phase 1 — Domain Exception Refactoring:** Cleaned up domain-level exception types for precision logic handling. Adopted discrete nested subclasses extending `DomainException` instead of raw strings. Full sealed hierarchy: `RemoteException`, `MangaException`, `CacheException`, `FavoritesHistoryException`, `UserException`. Every subtype has a concrete throw site in the data layer.

- [x] **Phase 2 — Domain Enum Wiring:** All 5 domain enums wired end-to-end across layers. `MangaLanguage` / `CategoryType` / criteria enums (`MangaSortCriteria*`, `MangaStatusFilter*`, `MangaContentRating*`) fully active. ISO code and param mapping lives in the data layer; `@StringRes` UI label mapping lives in the presentation layer. `valueOf(name)` pattern used for zero-overhead cross-layer enum conversion.

- [x] **Phase 3 — Domain Model Cleanup:** All domain model property names renamed to domain-meaningful names (removing DTO/API naming leakage). Default constants moved into `companion object` blocks in each domain model. All data layer mappers reference these constants.

- [x] **Phase 4 — ImmutableList Adoption (presentation layer):** Ported all `List<T>` inside `*UiState` models and `@Composable` parameters to `ImmutableList<T>` / `PersistentList<T>` using `kotlinx-collections-immutable`. Eliminates redundant Compose recomposition passes caused by Java `List` stability inference. All lists transformed at the ViewModel boundary.

- [x] **Phase 5 — Presentation Layer Domain Isolation (Batches 0–7):** Removed all domain model types (`Manga`, `Chapter`, `ReadingHistory`, etc.) from composables and UiState classes. ViewModels are now the sole translation boundary. New `*UiModel` / `*UiError` naming convention enforced. Six new mapper objects added in `presentation/mapper/`. Fixed critical CA violation in `FavoritesContent` (direct data-layer mapper call). `ChapterPagesUiModel` URL construction moved to `ChapterPagesUiMapper`.

- [x] **Phase 6 — Domain Package Reorganisation:** Restructured the entire `domain/` layer into a consistent 4-group taxonomy (`manga`, `category`, `user`, `settings`) across all three sub-layers (`model/`, `repository/`, `usecase/`). See Architecture Decisions Log for rules applied. Data repositories also moved to feature-based sub-packages.

- [x] **Phase 7 — Presentation Model Package Reorganisation:** Moved all 9 flat `presentation/model/` files into feature sub-packages (`manga/`, `category/`, `user/`, `settings/`), mirroring the domain layer taxonomy. `criteria/` and `error/` sub-packages were already grouped and untouched. ~70 consumer files updated. Build clean; pushed to `main`.

- [x] **Phase 8 — Compose Performance Fixes:** Fixed all 6 confirmed recomposition/stability issues identified in audit. Build clean; committed to `main` (`642d0f3`). Full deep-dive audit confirmed zero remaining issues across all 7 stability categories.
  - `VerticalGridMangaList` — stable `items` + `manga.id` key (was `itemsIndexed` + `"${id}_$index"`)
  - `CategoriesUiState` — `ImmutableMap` (was `Map`)
  - `CategoriesViewModel` — `.toImmutableMap()` at build site
  - `ReaderScreen` — `remember(chapterPagesUiState)` for page strings
  - `MenuDrawer` — `remember(...)` around `persistentListOf` menu items
  - `MangaItem` — `remember(manga.id)` lambda memoization

## Current Phase

- [ ] **QA / Smoke Testing:** Verify application behaviour after all structural and performance refactoring. No code changes expected — compile + manual UI verification only.
  - Build gate: `./gradlew :app:compileDebugKotlin` must produce 0 errors.
  - Screens to test: **Reader** (page counter), **Home / Manga list** (grid scrolling + tap), **Categories** (group rendering).

## Upcoming

- [ ] **Recomposition metrics validation:** Run Layout Inspector → Recomposition Counts in a debug build to quantify improvement from Phase 4 + Phase 8 changes.
- [ ] **New feature work** — project is architecturally complete; no further structural refactoring known.

---

## Architecture Decisions Log

### Exception Hierarchy
- Domain exceptions use discrete nested sealed subclasses. Every subtype has a concrete throw site in the data layer. `CancellationException` is always rethrown before `catch (e: Exception)` in ViewModels.
- `RemoteException` is cross-cutting (not manga-specific) — network failures affect all Retrofit repos.
- `FavoritesHistoryException` trimmed to `PermissionDenied` only — idempotent ops (`AlreadyExists`, `NotFound`) removed as they have no user-visible benefit.

### Domain Enum Wiring
- ISO codes and API param strings live in the **data layer** (`ParamMapper`). `@StringRes` UI labels live in the **presentation layer** (`LanguageMapper`, criteria mappers). Domain enums are pure.
- `valueOf(name)` pattern: enum names are kept identical across layers so conversion is zero-overhead.

### ImmutableList as strict collection type
- `ImmutableList<T>` (kotlinx-collections-immutable) is the enforced type across all `@Composable` parameters, `*UiState` models, and their Flow initializers.
- `ImmutableMap<K,V>` used for map-typed UiState fields (`CategoriesUiState.categoryMap`).
- Mutable accumulation pattern: `(oldList + newList).toPersistentList()` — never `.addAll()` on an immutable variable.

### Presentation Layer Domain Isolation
- Naming: data/enum UI models → `*UiModel`; error sealed classes → `*UiError`; state classes → `*UiState`.
- ViewModels consume domain types from use cases and emit `*UiModel` types to composables. No composable imports from `domain.*`.
- Reverse mappers (`UserUiModel.toDomainUser()`) exist only where a VM must pass UI state back into a domain use case.
- `MangaDetailsViewModel` keeps `private val _domainManga` for `addToFavoritesUseCase` and `private val _readingHistoryList` for domain logic (`findContinueTarget`).

### Domain Package Taxonomy (Phase 6 + 7)
- All layers use the same 4 top-level groups: **manga**, **category**, **user**, **settings**.
- **1-file rule:** secondary sub-packages only created when they hold ≥2 files. Top-level domain groups kept regardless of file count.
- **criteria/ exception:** `domain/model/criteria/` is not merged into `manga/` — criteria models are consumed by both manga and category operations.
- **usecase/ sub-grouping:** `usecase/manga/cache/` (4), `user/favorite/` (4), `user/history/` (3), `user/profile/` (3) are valid second-level groups.
- **repository/ stays flat within groups:** 1 file per interface, so no second-level sub-packages.

### Mapper Packages Stay Flat
- `presentation/mapper/` stays flat (11 files) — `UiErrorMapper` and `CriteriaMapper` are cross-feature adapters. Threshold for splitting: ~15 files, only valid axis is `entity/` vs `infra/`.
- `data/mapper/` stays flat (9 files) — `ApiParamMapper` and `ExceptionMapper` span features. Same threshold and axis apply.

### Compose Performance Rules (Phase 8)
- Lazy list keys must be stable domain IDs, never `"${id}_$index"` compound keys (index drift causes full tail recomposition on mutations). Exception: lists replaced wholesale (not mutated) can use compound keys safely.
- Map-typed UiState fields must use `ImmutableMap` / `persistentMapOf()`, not `Map` / `emptyMap()`.
- String derivations from UiState in composables must be wrapped in `remember(uiState)`.
- Static lists built from `stringResource` calls must extract the strings first, then wrap list construction in `remember(...)` keyed on those strings.
- Click lambdas that capture a stable ID should be wrapped in `remember(id)` to prevent Card/Button recomposition.
