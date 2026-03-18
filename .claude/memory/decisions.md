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
