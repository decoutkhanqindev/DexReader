## 2026-03-18

### Decision: Move Status/ContentRating UiModels from `criteria/filter/` to `manga/`

**What was decided:**
`MangaStatusUiModel` and `MangaContentRatingUiModel` (previously `MangaStatusFilterUiModel` /
`MangaContentRatingFilterUiModel`) were moved from `presentation/model/criteria/filter/` to
`presentation/model/manga/`.

**Reasoning:**
The `Filter` suffix and `criteria/filter/` location implied these types were query/filter-only
constructs. In practice, `MangaUiModel` uses them as display properties (`val status`,
`val contentRating`). They are manga attributes, not filter parameters. The domain layer had already
been cleaned up in a prior session (promoting `MangaStatus`/`MangaContentRating` to
`domain/model/manga/`), so the presentation layer needed to align.

**Alternatives considered:**

- Keep them in `criteria/filter/` with a note — rejected because it perpetuates the misleading name
  and location.
- Create a separate `presentation/model/manga/attributes/` sub-package — rejected as unnecessary
  nesting; `manga/` package is the natural home alongside `MangaUiModel`.

**Scope boundary:**
Sort criteria (`MangaSortCriteriaUiModel`, `MangaSortOrderUiModel`) were deliberately left untouched
in `criteria/sort/` — they genuinely describe query behavior, not manga attributes.

---

### Decision: Absorb status/rating mappers into `MangaUiMapper` instead of a new dedicated mapper

**What was decided:**
The 4 extension functions (`toMangaStatusUiModel`, `toMangaStatus`, `toMangaContentRatingUiModel`,
`toMangaContentRating`) were moved into the `MangaUiMapper` object.

**Reasoning:**
These mappers exist to support `Manga.toMangaUiModel()` and `FavoriteManga.toMangaUiModel()`.
Keeping them co-located with the manga mapping logic avoids creating a dedicated mapper object for
just 4 trivial `valueOf` calls. `CriteriaMapper` is now strictly sort-only, which matches its
remaining purpose.

**Alternatives considered:**

- Create a new `MangaAttributeUiMapper` — rejected as over-engineering for 4 one-liner functions.

---

### Decision: Fix `ReaderViewModel` nullable mismatch with `?: ""`

**What was decided:**
`chapter.volume ?: ""`, `chapter.number ?: ""`, `chapter.title ?: ""` added when copying into
`ReaderUiState`.

**Reasoning:**
`Chapter` domain model declares these fields as `String?` (optional metadata). `ReaderUiState` uses
`String` with `""` defaults. The `?: ""` fallback is consistent with the existing `""` defaults in
`ReaderUiState` and with how other nullable domain fields are handled throughout the codebase (e.g.,
`description ?: Manga.DEFAULT_DESCRIPTION` in `MangaUiMapper`).

**Alternatives considered:**

- Make `ReaderUiState` fields nullable — rejected because it would require null-checks across the UI
  layer and the empty-string default is semantically correct (renders nothing when unknown).

---

## 2026-03-18 (session 2)

### Decision: Split `domain/model/` into `domain/entity/` + `domain/value/`

**What was decided:**
All files in `domain/model/` were split into two new sub-packages:

- `domain/entity/` — ID-bearing business objects with identity (`Manga`, `Chapter`, `ChapterPages`,
  `FavoriteManga`, `Category`, `User`, `ReadingHistory`)
- `domain/value/` — Immutable, identity-free enums/value types (`MangaStatus`, `MangaContentRating`,
  `MangaLanguage`, `CategoryType`, `MangaSortCriteria`, `MangaSortOrder`, `ThemeMode`)

No class names changed; only package paths changed.

**Reasoning:**
Strict Clean Architecture (Uncle Bob) separates the domain's innermost ring into:

- **Entities**: objects with identity and lifecycle (carry an `id` field or are the central business
  object)
- **Value objects**: describe or qualify entities, are immutable, have no identity of their own

The previous `domain/model/` conflated both concepts in the same package, making it harder to reason
about the domain ring's structure. This split makes the architectural intent explicit.

**Classification rationale:**

- `Manga`, `Chapter`, `ChapterPages`, `FavoriteManga` → entity (have IDs, represent business
  objects)
- `Category` → entity (has `id: String`, referenced by `Manga`)
- `User`, `ReadingHistory` → entity (have identity, track user state)
- `MangaStatus`, `MangaContentRating`, `MangaLanguage` → value (enum descriptors of manga
  attributes)
- `CategoryType` → value (enum qualifier for Category)
- `MangaSortCriteria`, `MangaSortOrder` → value (query parameter descriptors, no identity)
- `ThemeMode` → value (UI/settings enum, no identity)

**Alternatives considered:**

- Keep `domain/model/` as-is — rejected, it conflates entity and value concepts
- Use `domain/entity/` only and put value types in `domain/model/` — rejected, inconsistent naming
- Nest values as inner classes of their entity — rejected, overly tight coupling and harder to reuse

**Implementation note:**
`Manga.kt` and `Category.kt` required manual cross-package imports added after the split, since
`MangaStatus`/`MangaContentRating`/`MangaLanguage` (used by `Manga`) and `CategoryType` (used by
`Category`) moved to a sibling package (`domain.value.*`) rather than staying in the same package.

---

## 2026-03-18 (session 3)

### Decision: `domain/repository/` and
`domain/usecase/` do NOT need structural changes after entity/value split

**What was decided:**
No structural (package/folder) changes are needed for `domain/repository/` or `domain/usecase/`
as a result of the `domain/model/` → `domain/entity/` + `domain/value/` split. Only import paths
were updated (already done by the bulk sed migration).

**Reasoning:**
The `entity`/`value` split is a **type taxonomy** — it answers "is this an identity-bearing object
or a pure descriptor?" Repositories and use cases are not types; they are operations and gateways.
Their sub-package organization by feature area (`manga/`, `category/`, `user/`, `settings/`) answers
a different and correct question: "what feature does this operation serve?"

In Clean Architecture:

- `domain/repository/` interfaces are gateways defined in the domain; their organization reflects
  *what data they manage*, not *what kind of type the result is*
- `domain/usecase/` classes are application-specific business rules; their organization reflects
  *what operation they perform*, not *what type their return value is*

Value objects appear in repositories only as *query parameters* (e.g.,
`statusFilter: List<MangaStatus>`)
— they describe the query, not the stored thing. This confirms the feature-area packaging is
correct.

**Verified by reading:**

- `MangaRepository.kt` — `import domain.entity.manga.Manga` ✓
- `FavoritesRepository.kt` — `import domain.entity.manga.FavoriteManga` ✓
- `GetMangaDetailsUseCase.kt` — `import domain.entity.manga.Manga` ✓
- `GetMangaListByCategoryUseCase.kt` — all 5 imports updated correctly ✓

**Alternatives considered:**

- Mirror the entity/value split in repository sub-packages (e.g., `repository/entity/manga/`) —
  rejected because repositories are operations/gateways, not type definitions; such nesting would
  be needless complexity

---

## 2026-03-19

### Decision: `presentation/value/` + `*Value` suffix for presentation value enums (supersedes
`enum/` plan)

**What was decided:**
The 7 presentation enum types should live in `presentation/value/` with `*Value` suffix:

- `MangaStatusValue`, `MangaLanguageValue`, `MangaContentRatingValue` → `presentation/value/manga/`
- `CategoryTypeValue` → `presentation/value/category/`
- `MangaSortCriteriaValue`, `MangaSortOrderValue` → `presentation/value/criteria/`
- `ThemeModeValue` → `presentation/value/settings/`

**Reasoning:**
Mirrors the domain layer's naming convention exactly:

- Domain: `domain/entity/` (entities) + `domain/value/` (value objects, e.g. `MangaStatus`,
  `ThemeMode`)
- Presentation: `presentation/model/` (data models) + `presentation/value/` (value enums)

This creates a clean parallel between the two layers: `domain/value/MangaStatus` ↔
`presentation/value/MangaStatusValue`.
The `*Value` suffix on the presentation side preserves the layer distinction (avoids name collision
with domain types in mapper files, e.g. `MangaStatus` vs `MangaStatusValue`).

**Supersedes:**
The written plan used `presentation/enum/` + `*Enum` suffix. This was implemented this session but
subsequently overridden by an intentional external edit to `MangaModel.kt` which uses `*Value`
naming.
The `enum/` + `*Enum` approach is discarded.

**Status:** Migration from `*Enum` → `*Value` still needs to be applied to ~30+ consumer files.
`MangaModel.kt` already uses `*Value` (the target state). `presentation/value/` directory not yet
created.

**Alternatives considered (from original plan):**

- `presentation/enum/` + `*Enum` — implemented then superseded; cleaner semantically for enums but
  breaks the domain/presentation symmetry
- `presentation/model/value/` — rejected (redundant nesting of `model/` in path)
- No suffix — rejected (name collision in mapper files between `MangaStatus` domain type and
  presentation type)

**Status (updated 2026-03-20):** Migration fully completed — all 7 `*Value` files exist in
`presentation/value/`. Previous blocker is resolved.

---

## 2026-03-20

### Decision: `MenuItem` data class → `MenuItemValue` enum in `presentation/value/menu/`

**What was decided:**
The `MenuItem` data class (3 mutable constructor params: `id`, `title`, `icon`) was replaced with a
`MenuItemValue` enum in `presentation/value/menu/`. The six fixed menu entries are now enum constants
with their display properties defined at declaration time.

**Final shape of `MenuItemValue` (after external edits this session):**
```kotlin
enum class MenuItemValue(@param:StringRes val nameRes: Int, val icon: ImageVector) {
  HOME, CATEGORIES, FAVORITES, HISTORY, PROFILE, SETTINGS;
  companion object {
    fun MenuItemValue.toNavRoute() = when(this) { HOME -> NavRoute.Home, ... }
  }
}
```
Note: original implementation had `titleRes` + computed `val id = name.lowercase()`. External edits
renamed `titleRes` → `nameRes`, removed `id`, and added `toNavRoute()` companion.

**Reasoning:**
- Six entries are fixed — a data class with runtime-constructed instances adds verbosity
- Matches the existing `ThemeModeValue` pattern for fixed-option UI enums
- `MenuDrawer` previously required 6 `stringResource()` calls + a 6-key `remember` — the enum
  collapses this to `remember { MenuItemValue.entries.toPersistentList() }`
- `toNavRoute()` companion makes navigation type-safe (no more `when (itemId)` string switches)

**Alternatives considered:**
- Keep `MenuItem` data class but extract constants — rejected, still requires runtime construction
- Place in `presentation/screens/common/menu/` package — rejected, value types belong in `value/`
  per established project convention

**Open question (still unresolved as of 2026-03-20 session 2):**
`MenuDrawer.kt` public signature uses `selectedItemId: String` but `MenuBody` now requires
`selectedItem: MenuItemValue`. Two paths forward:
- (a) Bridge internally with `entries.find { it.name.lowercase() == selectedItemId }`
- (b) Propagate `MenuItemValue` through `BaseScreen` and all callers for full type safety
No decision made — needs to be resolved at next session start.

**Status (updated 2026-03-20 session 3):** Resolved via type-safe navigation migration commit
`267105d`. `NavGraph.kt` and all callers updated; `MenuItemValue.toNavRoute()` companion used.

---

## 2026-03-20 (session 3)

### Decision: Mapper functions return nullable instead of throwing domain exceptions

**What was decided:**
`ChapterMapper.toChapter()` returns `Chapter?` (was `Chapter`, threw `ChapterNotFound`).
`ChapterPagesMapper.toChapterPages()` returns `ChapterPages?` (was `ChapterPages`, threw
`ChapterDataNotFound` twice). Domain exception throws moved to the calling repository.

**Reasoning:**
Mappers are pure data-transformation functions. Deciding that "null mangaId = ChapterNotFound" is a
**business rule** — that decision belongs in the repository (which understands the use-case context),
not in a mapper (which only knows how to transform shapes). This follows Clean Architecture's
Dependency Rule: mappers should not import domain exceptions.

For the chapter list use case specifically, one malformed API response item (missing `mangaId`)
should not abort the entire page. `mapNotNull` correctly skips it while preserving the rest.

**Alternatives considered:**
- Keep throws in mappers — rejected (architectural boundary violation: mapper imports domain exception)
- Throw in mapper, catch in repo — rejected (still couples mapper to domain exception hierarchy)

---

### Decision: `FavoriteMangaModel` (5 fields) instead of `MangaModel` (13 fields) for Favorites

**What was decided:**
Created `presentation/model/manga/FavoriteMangaModel.kt` with only the 5 fields `FavoriteManga`
actually carries: `id`, `title`, `coverUrl`, `author`, `status`. `FavoritesContent` uses a new
`FavoriteMangaItem` composable instead of the shared `MangaItem`/`VerticalGridMangaList`.

**Reasoning:**
The old `FavoriteManga.toMangaModel()` produced a `MangaModel` with 7 hollow `""` / `persistentListOf()`
values (`description`, `artist`, `categories`, `contentRating`, `year`, `availableLanguages`,
`latestChapter`, `updatedAt`). These hollows are never rendered in `FavoritesContent` — `MangaItem`
only shows `id`, `title`, `coverUrl`, `author`, `status`. Using a 13-field model for a 5-field use
case is a type-partitioning mistake: it implies the missing fields exist but are empty, rather than
clearly communicating "this model only has these 5 fields."

**Alternatives considered:**
- Keep `MangaModel` with hollow fields — rejected (misleading contract, wastes allocation)
- Add `FavoriteManga.author/status` to a lighter wrapper of `MangaModel` — rejected (unnecessary
  indirection; a dedicated model is clearer)
- Reuse `MangaItem` via a conversion — rejected (would require fake hollow fields to construct `MangaModel`)

---

### Decision: `UserError.Unexpected` replaces `null` return from `toUserError()`

**What was decided:**
Added `data object Unexpected : UserError(R.string.oops_something_went_wrong_please_try_again)` to
`UserError`. `ErrorMapper.toUserError()` now returns `UserError` (non-nullable); `else -> null`
replaced with `else -> UserError.Unexpected`.

**Reasoning:**
A nullable return type on `toUserError()` leaks a null contract that all 3 auth VMs must silently
accommodate via `else ->` (i.e., the null case is handled implicitly by doing nothing). Making the
return non-nullable with an explicit `Unexpected` sentinel:
1. Makes the unrecognized-exception case explicit and visible in the type system
2. Allows auth VMs to show a generic error instead of silently ignoring unexpected exceptions
3. Is consistent with how `toFeatureError()` already works (always returns `FeatureError`, never null)

**Alternatives considered:**
- Keep nullable — rejected (implicit null handling is a hidden contract)
- Throw in `else` branch — rejected (would change caller behavior significantly; `Unexpected` is safer)

---

### Decision: `String?.toMangaLanguage()` uses nullable receiver (consistency fix)

**What was decided:**
`ApiParamMapper.toMangaLanguage()` receiver changed from `String` to `String?`. Returns
`MangaLanguage.UNKNOWN` for null input (handled by `?: MangaLanguage.UNKNOWN` fallback when no param
entry matches null).

**Reasoning:**
`toMangaStatus()` and `toMangaContentRating()` already use `String?` receivers and handle null
gracefully. `toMangaLanguage()` using a non-nullable receiver was inconsistent — callers had to add
`?.` or `!!` before calling on nullable strings. `attributes?.translatedLanguage.toMangaLanguage()`
is cleaner than `attributes?.translatedLanguage?.toMangaLanguage() ?: Chapter.DEFAULT_LANGUAGE`.

`MangaLanguage.UNKNOWN == Chapter.DEFAULT_LANGUAGE` (by value), so the behavioral outcome is
identical — this is purely a type-consistency improvement.

**Alternatives considered:**
- Keep non-nullable, require callers to use `?.` — rejected (inconsistent with sibling functions)
