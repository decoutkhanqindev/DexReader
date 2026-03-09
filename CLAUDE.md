# DexReader — Claude Code Session Notes

## Status

**Current Phase:** Presentation Layer — Full Domain Model Isolation
**Overall Progress:** Batch 0 (~60% done) + Batch 1 (100% done). Batches 2–7 not started.

---

## Completed This Session

- **Batch 1 — All 12 new files created** (no modifications to existing files):
  - New models: `CategoryUiModel`, `ChapterUiModel`, `ChapterPagesUiModel`, `ReadingHistoryUiModel`, `UserUiModel`, `MangaUiModel`
  - New mappers: `CategoryUiMapper`, `ChapterUiMapper`, `ChapterPagesUiMapper`, `ReadingHistoryUiMapper`, `UserUiMapper`, `MangaUiMapper`
  - All in `presentation/model/` and `presentation/mapper/`

- **Batch 0 — Partial (renamed type files + ~40 of ~58 consumer files updated):**
  - Deleted 9 old files (old Option/Name/Error type files)
  - Created 9 new renamed files (UiModel/UiError naming)
  - Updated mappers: `LanguageMapper`, `ErrorMapper`, `ThemeMapper`, `CriteriaMapper`
  - Updated auth screens, categories, category_details, some settings
  - **NOT YET DONE** (18 files still reference old names — see below)

---

## Next Session — Start Here

**Step 1: Finish Batch 0 — update these 18 remaining files** that still use old type names:

```
presentation/screens/manga_details/components/chapters/ChapterLanguageBottomSheet.kt
presentation/screens/manga_details/components/chapters/MangaChaptersHeader.kt
presentation/screens/manga_details/components/chapters/MangaChaptersSection.kt
presentation/screens/manga_details/components/MangaDetailsContent.kt
presentation/screens/manga_details/MangaDetailsViewModel.kt
presentation/screens/profile/components/ProfileContent.kt
presentation/screens/profile/ProfileUiState.kt
presentation/screens/profile/ProfileViewModel.kt
presentation/screens/reader/ReaderUiState.kt
presentation/screens/reader/ReaderViewModel.kt
presentation/screens/search/SearchViewModel.kt
presentation/screens/search/SuggestionsUiState.kt
presentation/screens/settings/components/SettingsContent.kt
presentation/screens/settings/components/ThemeOptionItem.kt
presentation/screens/settings/components/ThemeOptionList.kt
presentation/screens/settings/SettingsScreen.kt
presentation/screens/settings/SettingsViewModel.kt
presentation/theme/Theme.kt
```

For each file: replace old type names (`FeatureError→FeatureUiError`, `UserError→UserUiError`, `ThemeOption→ThemeUiModel`, `MangaLanguageName→MangaLanguageUiModel`) and update imports. Also rename the method call `toFeatureError()→toFeatureUiError()`, `toUserError()→toUserUiError()`, `toThemeOption()→toThemeUiModel()` at call sites.

**Step 2: Run Batches 2–7 in parallel** (after Batch 0 is 100% done):
- **Batch 2** (Category): `CategoriesUiState`, `CategoriesViewModel`, `CategoryItem`, `CategoryList`, `CategoryTypeSection` — swap `Category` → `CategoryUiModel`
- **Batch 3** (User): `ProfileUiState`, `ProfileViewModel`, `ProfileContent` — swap `User` → `UserUiModel`; keep private `currentDomainUser: User?` in VM for use case calls
- **Batch 4+5+6** (Manga/Chapter/ReadingHistory): Large batch, ~25 files — see PROGRESS.md
- **Batch 7** (ChapterPages): `ReaderUiState`, `ReaderViewModel`, `ReaderContent` — swap `ChapterPages` → `ChapterPagesUiModel`; use `pageImageUrls` field

**Step 3: Verify build:**
```
./gradlew :app:compileDebugKotlin
./gradlew :app:assembleDebug
```

---

## Important Context

### Type Rename Map (Batch 0)
| Old | New |
|---|---|
| `MangaLanguageName` | `MangaLanguageUiModel` |
| `CategoryTypeOption` | `CategoryTypeUiModel` |
| `ThemeOption` | `ThemeUiModel` |
| `MangaStatusFilterOption` | `MangaStatusFilterUiModel` |
| `MangaContentRatingFilterOption` | `MangaContentRatingFilterUiModel` |
| `MangaSortCriteriaOption` | `MangaSortCriteriaUiModel` |
| `MangaSortOrderOption` | `MangaSortOrderUiModel` |
| `FeatureError` | `FeatureUiError` |
| `UserError` | `UserUiError` |

### Mapper Method Renames (Batch 0)
- `toMangaLanguageName()` → `toMangaLanguageUiModel()`
- `toFeatureError()` → `toFeatureUiError()`
- `toUserError()` → `toUserUiError()`
- `toThemeOption()` → `toThemeUiModel()`

### Critical gotchas for Batches 3, 4, 5, 6
- **ProfileViewModel**: Must add `private var currentDomainUser: User? = null`. `updateCurrentUser(User?)` sets both the domain var AND maps to `UserUiModel` for state. `updateUserProfile()` uses `currentDomainUser` for the use case, NOT `currentUiState.currentUser`.
- **MangaDetailsViewModel**: Must add `private var _domainManga: Manga? = null`. `addToFavoritesUseCase` still takes domain `Manga`, so store original before mapping. `availableLanguages` StateFlow simplifies: after Batch 4, just do `state.manga.availableLanguages` (already `List<MangaLanguageUiModel>` from `MangaUiModel`).
- **MangaDetailsViewModel readingHistory**: Keep `_readingHistoryList: MutableStateFlow<List<ReadingHistory>>` private (used for `ReadingHistory.findContinueTarget()` domain logic). Expose `readingHistoryList: StateFlow<List<ReadingHistoryUiModel>>` via `.map { list -> list.map { it.toReadingHistoryUiModel() } }.stateIn(...)`.
- **MangaDetailsViewModel startedChapter**: Change from `StateFlow<Chapter?>` to `startedChapterId: StateFlow<String?>`. Composables only need ID for navigation.
- **ActionButtonsSection**: Change `startedChapter: Chapter?` → `startedChapterId: String?`, add `mangaId: String` param. `MangaDetailsContent.kt` passes `manga.id` for this.
- **FavoritesContent**: Remove `import data.mapper.FavoriteMangaMapper.toManga` and remove `.map { it.toManga() }` call. State is now `BasePaginationUiState<MangaUiModel>` directly.
- **FavoriteMangaMapper**: Delete `toManga()` function from `data/mapper/FavoriteMangaMapper.kt`.
- **ChapterPagesUiModel**: `pageImageUrls` field is pre-computed URLs. In `ReaderContent.kt`, change `chapterPages.pages` → `chapterPages.pageImageUrls`.

### Domain Exception naming (actual current code, NOT as in memory.md)
The actual code uses `InfrastructureException`, `BusinessException`, `ValidationException` (NOT `RemoteException`/`MangaException` as stated in memory.md — memory.md is OUTDATED for exceptions). Do not change any exception types.

---

## Files Modified

### New files created (Batch 1):
- `presentation/model/CategoryUiModel.kt`
- `presentation/model/ChapterUiModel.kt`
- `presentation/model/ChapterPagesUiModel.kt`
- `presentation/model/MangaUiModel.kt`
- `presentation/model/ReadingHistoryUiModel.kt`
- `presentation/model/UserUiModel.kt`
- `presentation/mapper/CategoryUiMapper.kt`
- `presentation/mapper/ChapterUiMapper.kt`
- `presentation/mapper/ChapterPagesUiMapper.kt`
- `presentation/mapper/MangaUiMapper.kt`
- `presentation/mapper/ReadingHistoryUiMapper.kt`
- `presentation/mapper/UserUiMapper.kt`

### New files created (Batch 0 renames):
- `presentation/model/MangaLanguageUiModel.kt`
- `presentation/model/CategoryTypeUiModel.kt`
- `presentation/model/ThemeUiModel.kt`
- `presentation/model/criteria/filter/MangaStatusFilterUiModel.kt`
- `presentation/model/criteria/filter/MangaContentRatingFilterUiModel.kt`
- `presentation/model/criteria/sort/MangaSortCriteriaUiModel.kt`
- `presentation/model/criteria/sort/MangaSortOrderUiModel.kt`
- `presentation/model/error/FeatureUiError.kt`
- `presentation/model/error/UserUiError.kt`

### Deleted files (Batch 0):
- `presentation/model/MangaLanguageName.kt`
- `presentation/model/CategoryTypeOption.kt`
- `presentation/model/ThemeOption.kt`
- `presentation/model/criteria/filter/MangaStatusFilterOption.kt`
- `presentation/model/criteria/filter/MangaContentRatingFilterOption.kt`
- `presentation/model/criteria/sort/MangaSortCriteriaOption.kt`
- `presentation/model/criteria/sort/MangaSortOrderOption.kt`
- `presentation/model/error/FeatureError.kt`
- `presentation/model/error/UserError.kt`

### Updated mappers (Batch 0):
- `presentation/mapper/LanguageMapper.kt` — method renames
- `presentation/mapper/ErrorMapper.kt` — method renames + type refs
- `presentation/mapper/ThemeMapper.kt` — method renames + type refs
- `presentation/mapper/CriteriaMapper.kt` — type refs

### Updated screens (Batch 0, partial):
- All auth screens and UiStates (UserError → UserUiError)
- All categories screens (CategoryTypeOption → CategoryTypeUiModel)
- All category_details sort/filter components (criteria Option → UiModel)
- Settings UiState (ThemeOption → ThemeUiModel)
- NavGraph.kt

### NOT YET UPDATED (18 files, Batch 0 incomplete):
See "Next Session — Start Here" above.
