## 2026-03-18

### Decision: Move Status/ContentRating UiModels from `criteria/filter/` to `manga/`

**What was decided:**
`MangaStatusUiModel` and `MangaContentRatingUiModel` (previously `MangaStatusFilterUiModel` / `MangaContentRatingFilterUiModel`) were moved from `presentation/model/criteria/filter/` to `presentation/model/manga/`.

**Reasoning:**
The `Filter` suffix and `criteria/filter/` location implied these types were query/filter-only constructs. In practice, `MangaUiModel` uses them as display properties (`val status`, `val contentRating`). They are manga attributes, not filter parameters. The domain layer had already been cleaned up in a prior session (promoting `MangaStatus`/`MangaContentRating` to `domain/model/manga/`), so the presentation layer needed to align.

**Alternatives considered:**
- Keep them in `criteria/filter/` with a note — rejected because it perpetuates the misleading name and location.
- Create a separate `presentation/model/manga/attributes/` sub-package — rejected as unnecessary nesting; `manga/` package is the natural home alongside `MangaUiModel`.

**Scope boundary:**
Sort criteria (`MangaSortCriteriaUiModel`, `MangaSortOrderUiModel`) were deliberately left untouched in `criteria/sort/` — they genuinely describe query behavior, not manga attributes.

---

### Decision: Absorb status/rating mappers into `MangaUiMapper` instead of a new dedicated mapper

**What was decided:**
The 4 extension functions (`toMangaStatusUiModel`, `toMangaStatus`, `toMangaContentRatingUiModel`, `toMangaContentRating`) were moved into the `MangaUiMapper` object.

**Reasoning:**
These mappers exist to support `Manga.toMangaUiModel()` and `FavoriteManga.toMangaUiModel()`. Keeping them co-located with the manga mapping logic avoids creating a dedicated mapper object for just 4 trivial `valueOf` calls. `CriteriaMapper` is now strictly sort-only, which matches its remaining purpose.

**Alternatives considered:**
- Create a new `MangaAttributeUiMapper` — rejected as over-engineering for 4 one-liner functions.

---

### Decision: Fix `ReaderViewModel` nullable mismatch with `?: ""`

**What was decided:**
`chapter.volume ?: ""`, `chapter.number ?: ""`, `chapter.title ?: ""` added when copying into `ReaderUiState`.

**Reasoning:**
`Chapter` domain model declares these fields as `String?` (optional metadata). `ReaderUiState` uses `String` with `""` defaults. The `?: ""` fallback is consistent with the existing `""` defaults in `ReaderUiState` and with how other nullable domain fields are handled throughout the codebase (e.g., `description ?: Manga.DEFAULT_DESCRIPTION` in `MangaUiMapper`).

**Alternatives considered:**
- Make `ReaderUiState` fields nullable — rejected because it would require null-checks across the UI layer and the empty-string default is semantically correct (renders nothing when unknown).

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
- **Entities**: objects with identity and lifecycle (carry an `id` field or are the central business object)
- **Value objects**: describe or qualify entities, are immutable, have no identity of their own

The previous `domain/model/` conflated both concepts in the same package, making it harder to reason
about the domain ring's structure. This split makes the architectural intent explicit.

**Classification rationale:**
- `Manga`, `Chapter`, `ChapterPages`, `FavoriteManga` → entity (have IDs, represent business objects)
- `Category` → entity (has `id: String`, referenced by `Manga`)
- `User`, `ReadingHistory` → entity (have identity, track user state)
- `MangaStatus`, `MangaContentRating`, `MangaLanguage` → value (enum descriptors of manga attributes)
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

### Decision: `domain/repository/` and `domain/usecase/` do NOT need structural changes after entity/value split

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

Value objects appear in repositories only as *query parameters* (e.g., `statusFilter: List<MangaStatus>`)
— they describe the query, not the stored thing. This confirms the feature-area packaging is correct.

**Verified by reading:**
- `MangaRepository.kt` — `import domain.entity.manga.Manga` ✓
- `FavoritesRepository.kt` — `import domain.entity.manga.FavoriteManga` ✓
- `GetMangaDetailsUseCase.kt` — `import domain.entity.manga.Manga` ✓
- `GetMangaListByCategoryUseCase.kt` — all 5 imports updated correctly ✓

**Alternatives considered:**
- Mirror the entity/value split in repository sub-packages (e.g., `repository/entity/manga/`) —
  rejected because repositories are operations/gateways, not type definitions; such nesting would
  be needless complexity
