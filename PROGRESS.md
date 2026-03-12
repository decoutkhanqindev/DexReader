# Project Progress Tracker

## Completed Phases

- [x] **Phase 1 — Domain Exception Refactoring:** Cleaned up domain-level exception types for precision logic handling. Adopted discrete nested subclasses extending `BusinessException` instead of raw strings.
- [x] **Phase 2 — Camera State Management:** Fixed state consistency bugs in Compose Navigation by migrating visual triggers to ViewModel. Adopted shared ViewModels over NavArgs for persistent lifecycle states.
- [x] **Phase 3 — Camera Component Refactoring:** Segregated view responsibilities into reusable layout blocks.
- [x] **Phase 4 — ImmutableList Adoption (presentation layer):** Ported all `List<T>` inside `*UiState` models and `@Composable` parameters to `ImmutableList<T>` / `PersistentList<T>` using `kotlinx-collections-immutable`. Eliminates redundant Compose recomposition passes caused by Java `List` stability inference. All lists transformed at the ViewModel boundary.
- [x] **Phase 5 — Presentation Layer Domain Isolation (Batches 0–7):** Removed all domain model types (`Manga`, `Chapter`, `ReadingHistory`, etc.) from composables and UiState classes. ViewModels are now the sole translation boundary. New `*UiModel` / `*UiError` naming convention enforced. Six new mapper objects added in `presentation/mapper/`. Fixed critical CA violation in `FavoritesContent` (direct data-layer mapper call).
- [x] **Phase 6 — Domain Package Reorganisation:** Restructured the entire `domain/` layer into a consistent 4-group taxonomy (`manga`, `category`, `user`, `settings`) across all three sub-layers. See Architecture Decisions Log for rules applied.

## Current Phase

- [ ] **QA / Smoke Testing:** Verify application behaviour after all structural refactoring. No code changes expected — compile + manual UI verification only.
  - Build gate: `./gradlew :app:compileDebugKotlin` must produce 0 errors.
  - Screens to test: **Reader** (ChapterPages URL flow), **Home / Manga list** (ImmutableList recomposition), **Categories grid** (CategoryType map grouping).

## Upcoming

- [ ] Performance validation: confirm Compose recomposition metrics show improvement or parity after ImmutableList + domain isolation changes.

---

## Architecture Decisions Log

### Exception Hierarchy
- Domain exceptions use discrete nested subclasses (`BusinessException.Resource`, etc.) instead of raw message strings, enabling exhaustive `when` handling in ViewModels without string matching.

### Shared ViewModels over NavArgs
- Persistent lifecycle states (e.g. camera flags `isReadyToAdd`, `isAlreadyInUse`) stored in scoped shared ViewModels instead of URL query parameters, decoupling navigation structure from business logic.

### ImmutableList as strict collection type
- `ImmutableList<T>` (kotlinx-collections-immutable) is the enforced type across all `@Composable` parameters, `*UiState` models, and their Flow initializers.
- Mutable accumulation pattern: `(oldList + newList).toPersistentList()` — never `.addAll()` on an immutable variable.
- `EnumEntries` must be mapped with `.toPersistentList()` when assigned to `ImmutableList` properties.

### Presentation Layer Domain Isolation
- Naming convention: data/enum UI models → `*UiModel` suffix; error sealed classes → `*UiError` suffix; state classes → `*UiState` (unchanged).
- ViewModels consume domain types from use cases and emit `*UiModel` types to composables. No composable imports from `domain.*`.
- Reverse mappers (`UserUiModel.toDomainUser()`) exist only where a VM must pass UI state back into a domain use case.

### Domain Package Taxonomy (Phase 6)
- All three `domain/` sub-layers use the same 4 top-level groups: **manga**, **category**, **user**, **settings**.
- **1-file rule:** secondary sub-packages (2nd level within a group) are only created when they hold ≥2 files. Top-level domain groups are kept regardless of file count.
- **criteria/ exception:** `domain/model/criteria/` is not merged into `manga/` because criteria models are consumed by both manga and category operations.
- **usecase/ sub-grouping:** `usecase/manga/cache/` (4 files) and `usecase/user/favorite/` (4), `user/history/` (3), `user/profile/` (3) are valid second-level groups by the ≥2-file rule.
- **repository/ stays flat within groups:** each repository interface is 1 file, so no second-level sub-packages are created inside `repository/manga/` or `repository/user/`.
