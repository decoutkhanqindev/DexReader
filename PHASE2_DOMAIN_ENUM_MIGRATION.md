# Phase 2: Presentation Layer Domain Enum Migration

## Summary

Phase 2 completes the domain enum wiring started in Phase 1 by removing all
string-based state from the category sort/filter UI and correcting the
`Manga.availableTranslatedLanguages` type.

---

## What Changed

### Item A — `CategoryDetailsCriteriaUiState` redesign

**Before:** 8 fields (4 nullable `*OrderId` strings + 2 `List<String>` + 2 selected-id strings)
**After:** 4 typed domain enum fields — no raw strings in UI state.

| Old field                                                                                                   | New field             | Type                             |
|-------------------------------------------------------------------------------------------------------------|-----------------------|----------------------------------|
| `selectedSortCriteriaId`, `lastUpdatedOrderId`, `followedCountOrderId`, `createdAtOrderId`, `ratingOrderId` | `sortCriteria`        | `MangaSortCriteria`              |
| `selectedSortOrderId`                                                                                       | `sortOrder`           | `MangaSortOrder`                 |
| `statusValueIds`                                                                                            | `statusFilter`        | `List<MangaStatusFilter>`        |
| `contentRatingValueIds`                                                                                     | `contentRatingFilter` | `List<MangaContentRatingFilter>` |

3 bridge helpers deleted from `CategoryDetailsViewModel`:

- `deriveSortParams()` — decoded nullable string fields back into domain enums
- `String.toMangaStatusFilter()` — decoded status string IDs
- `String.toMangaContentRatingFilter()` — decoded content rating string IDs

String ↔ enum conversion is now done **at the UI boundary** in
`updateSortingCriteria()` and `updateFilteringCriteria()` using `when` expressions
that map `SortCriteria`/`FilterValue` IDs to domain enums. Below the boundary,
only domain enums flow.

### Item B — `Manga.availableTranslatedLanguages` type fix

**Before:** `List<String>` (display names embedded in the domain layer — wrong layer)
**After:** `List<MangaLanguage>` (pure domain type)

- `MangaMapper.kt`: now calls `String.toMangaLanguage()` (ISO code → enum, data layer)
  instead of `String.toFullLanguageName()` (ISO code → display name, presentation concern)
- `MangaDetailsContent.kt`: converts at the read boundary with
  `.map { it.toDisplayName() }` before passing to `MangaChaptersSection`

---

## New File

### `util/CriteriaCodec.kt`

Parallel to `LanguageCodec.kt`. Domain enum → UI string ID conversions for the
sort/filter bottom sheets. Used by `SortBottomSheet` and `FilterBottomSheet` to
initialize local `rememberSaveable` state from the domain enum fields.

```
MangaSortCriteria  → SortCriteria.*.id   (toSortCriteriaId)
MangaSortOrder     → SortOrder.*.id      (toSortOrderId)
MangaStatusFilter  → FilterValue.*.id    (toFilterValueId)
MangaContentRatingFilter → FilterValue.*.id (toFilterValueId)
```

---

## Files Changed

| File                                                               | Change                                                                                                               |
|--------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| `util/CriteriaCodec.kt`                                            | **NEW** — domain enum → UI string ID codec                                                                           |
| `presentation/.../CategoryDetailsCriteriaUiState.kt`               | Full replacement: 8 strings → 4 domain enums                                                                         |
| `presentation/.../CategoryDetailsViewModel.kt`                     | Remove 3 bridges; simplify `updateSortingCriteria` + `updateFilteringCriteria`; direct enum reads in fetch functions |
| `presentation/.../sort/SortBottomSheet.kt`                         | Init local state via `CriteriaCodec` instead of reading old string fields                                            |
| `presentation/.../filter/FilterBottomSheet.kt`                     | Init local state via `CriteriaCodec` instead of reading old string fields                                            |
| `domain/model/Manga.kt`                                            | `availableTranslatedLanguages: List<String>` → `List<MangaLanguage>`                                                 |
| `data/mapper/MangaMapper.kt`                                       | `toFullLanguageName()` → `toMangaLanguage()`; remove wrong-layer import                                              |
| `presentation/.../manga_details/components/MangaDetailsContent.kt` | `.map { it.toDisplayName() }` at read boundary                                                                       |

---

## Architecture After Phase 2

```
API (ISO string)
    ↓  MangaMapper / ChapterMapper  [data layer]
       toMangaLanguage()
    ↓
Domain model  (MangaLanguage enum, MangaSortCriteria, etc.)
    ↓  read boundary in composables  [presentation layer]
       LanguageCodec.toDisplayName()
       CriteriaCodec.toSortCriteriaId() / toFilterValueId()
    ↓
UI strings (display names, button IDs)
```

String → enum conversion happens only at the **entry** boundary
(`updateSortingCriteria`, `updateFilteringCriteria` in the VM).
Everything below that boundary uses domain enums.

---

## Phase 1 + Phase 2 Status

| Enum                       | Data layer param                      | Domain model                                                          | Presentation           |
|----------------------------|---------------------------------------|-----------------------------------------------------------------------|------------------------|
| `MangaLanguage`            | `toParam()` ✓                         | `Chapter.translatedLanguage` ✓ `Manga.availableTranslatedLanguages` ✓ | `toDisplayName()` ✓    |
| `MangaSortCriteria`        | via `GetMangaListByCategoryUseCase` ✓ | `CategoryDetailsCriteriaUiState.sortCriteria` ✓                       | `toSortCriteriaId()` ✓ |
| `MangaSortOrder`           | `toParam()` ✓                         | `CategoryDetailsCriteriaUiState.sortOrder` ✓                          | `toSortOrderId()` ✓    |
| `MangaStatusFilter`        | `toParam()` ✓                         | `CategoryDetailsCriteriaUiState.statusFilter` ✓                       | `toFilterValueId()` ✓  |
| `MangaContentRatingFilter` | `toParam()` ✓                         | `CategoryDetailsCriteriaUiState.contentRatingFilter` ✓                | `toFilterValueId()` ✓  |

All 5 domain enums are fully wired end-to-end with no raw strings crossing layer boundaries.
