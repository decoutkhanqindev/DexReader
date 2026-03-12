## Status

`domain/` layer package reorganisation is complete. All three sub-layers (`model/`, `repository/`, `usecase/`) now share a consistent 4-group taxonomy: **manga**, **category**, **user**, **settings**. Project imports are fully updated. No stale references remain.

## Completed This Session

- **domain/model/** — moved 10 flat model files into 4 sub-packages (`manga/`, `category/`, `user/`, `settings/`); `criteria/` left untouched.
- **domain/repository/** — moved 8 flat repository interfaces into matching sub-packages (`manga/`, `category/`, `user/`, `settings/`).
- **domain/usecase/** — consolidated 8 groups → 4 top-level groups (`manga/`, `category/`, `user/`, `settings/`), merging `chapter/` + `cache/` → `manga/` and `favorites/` + `history/` → `user/`. User further sub-grouped: `manga/cache/`, `user/favorite/`, `user/history/`, `user/profile/`.
- **1-file rule applied** — flattened any secondary sub-package that contained exactly 1 file back to its parent (e.g. `repository/manga/chapter/` → `repository/manga/`). Top-level domain groups kept even when 1 file.
- Bulk import updates via Python script across ~130 consumer files (data layer, domain layer, presentation layer).

## Next Session - Start Here

Run `./gradlew :app:compileDebugKotlin` to verify the build is clean after all package moves.

Then perform manual smoke testing of the three high-risk screens:
1. **Reader** — open `ReaderViewModel.kt`, verify `ChapterPagesUiModel` page URL flow.
2. **Manga list** — open `HomeViewModel.kt`, verify `ImmutableList<MangaUiModel>` recomposition.
3. **Categories grid** — open `CategoriesViewModel.kt`, verify `Map<CategoryTypeUiModel, ImmutableList<CategoryUiModel>>`.

## Important Context

- **Package taxonomy (final):** every `domain/` sub-layer uses `manga / category / user / settings` as top-level groups. `repository/` and `usecase/` mirror each other; `usecase/` adds a second level inside `manga/` and `user/` for sub-concerns with ≥2 files.
- **1-file rule:** secondary sub-packages (2nd level within a group) are only created when they hold ≥2 files. Top-level domain groups (manga, category, user, settings) are kept regardless of file count — they are domain separators, not structural conveniences.
- **criteria/ stays separate:** `domain/model/criteria/filter/` and `domain/model/criteria/sort/` were not merged into `manga/` — criteria models are shared by both `manga/` and `category/` operations, so a standalone `criteria/` package is correct.
- **Import updates:** all import changes were applied via a Python regex script (`fix_imports.py`, deleted after each run) using `re.MULTILINE` exact-line matching to avoid partial-string collisions.
- **User-applied sub-grouping:** the user independently moved use cases into `usecase/manga/cache/`, `usecase/user/favorite/`, `usecase/user/history/`, `usecase/user/profile/` via the IDE. These are intentional and correct.

## Files Modified

**domain/model/** (10 files moved, package declarations updated):
- `manga/Manga.kt`, `manga/Chapter.kt`, `manga/ChapterPages.kt`, `manga/MangaLanguage.kt`, `manga/FavoriteManga.kt`
- `category/Category.kt`, `category/CategoryType.kt`
- `user/User.kt`, `user/ReadingHistory.kt`
- `settings/ThemeMode.kt`

**domain/repository/** (8 files moved, package declarations updated):
- `manga/MangaRepository.kt`, `manga/ChapterRepository.kt`, `manga/CacheRepository.kt`
- `category/CategoryRepository.kt`
- `user/UserRepository.kt`, `user/FavoritesRepository.kt`, `user/HistoryRepository.kt`
- `settings/SettingsRepository.kt`

**domain/usecase/** (14 files moved across consolidation + user sub-grouping):
- `manga/`: GetChapterDetailsUseCase, GetChapterListUseCase, GetChapterPagesUseCase (from `chapter/`)
- `manga/cache/`: AddChapterCacheUseCase, ClearExpiredCacheUseCase, DeleteChapterCacheUseCase, GetChapterCacheUseCase (from `cache/`)
- `user/favorite/`: AddToFavoritesUseCase, ObserveFavoritesUseCase, ObserveIsFavoriteUseCase, RemoveFromFavoritesUseCase
- `user/history/`: AddAndUpdateToHistoryUseCase, ObserveHistoryUseCase, RemoveFromHistoryUseCase

**Consumer files updated (~130 files):**
- All `data/mapper/`, `data/repository/` impls
- `di/RepositoryModule.kt`
- All `domain/repository/`, `domain/usecase/` files
- All `presentation/mapper/`, `presentation/screens/*/ViewModel.kt` files
