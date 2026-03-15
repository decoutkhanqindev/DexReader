## Status

Compose performance fixes complete. All 5 targeted recomposition/stability issues from the audit are resolved. Build is clean and committed to `main` (`642d0f3`). A full deep-dive audit with the `android-jetpack-compose-expert` skill confirmed zero remaining issues across all 7 stability categories.

The project is structurally complete and performance-clean. Ready for manual smoke testing or new feature work.

## Completed This Session

- **Fix 1 — `VerticalGridMangaList.kt`:** Replaced `itemsIndexed` + `"${id}_$index"` key with `items` + `manga.id` key. Eliminates tail-recomposition on list insertions/removals.
- **Fix 2 — `CategoriesUiState.kt`:** Changed `categoryMap` from `Map<…>` to `ImmutableMap<…>` with `persistentMapOf()` default. Compose stability checker now treats it as stable.
- **Fix 3 — `CategoriesViewModel.kt`:** Added `.toImmutableMap()` call on `associateWith { … }` result to satisfy the new `ImmutableMap` type.
- **Fix 4 — `ReaderScreen.kt`:** Wrapped `currentPage`/`totalPages` string derivation in `remember(chapterPagesUiState)`. Prevents string allocation on every recompose.
- **Fix 5 — `MenuDrawer.kt`:** Extracted all 6 `stringResource(…)` calls, then wrapped `persistentListOf(…)` in `remember(…)` keyed on the resolved strings. List is no longer rebuilt on every recompose.
- **Fix 6 — `MangaItem.kt`:** Memoized click lambda with `remember(manga.id)`. Prevents unnecessary Card recomposition from new lambda instances.
- **Full deep-dive audit:** `android-jetpack-compose-expert` skill verified all 170+ presentation files — zero remaining stability, key, `rememberSaveable`, or annotation issues found.
- All 6 fixes committed in a single commit: `perf: fix compose recomposition issues from audit` (`642d0f3`)

## Next Session - Start Here

Run `./gradlew :app:compileDebugKotlin` to confirm build is still clean (should be — last confirmed `BUILD SUCCESSFUL`).

Then perform manual smoke testing of the three high-risk screens:
1. **Reader** — open a chapter, swipe pages, verify page counter (`currentPage / totalPages`) updates correctly.
2. **Manga list (Home)** — scroll the grid, verify no jank; confirm `MangaItem` tap navigates to details.
3. **Categories grid** — open Categories, verify all `CategoryTypeUiModel` groups render with their `ImmutableList<CategoryUiModel>` members.

If smoke testing passes, the next logical step is:
- **New feature work** (the project is architecturally complete), or
- **Compose recomposition metrics** — run Layout Inspector → Recomposition Counts in a debug build to quantify improvement.

## Architectural Decision: `presentation/mapper/` Stays Flat

**Decision (confirmed):** Do not reorganize `presentation/mapper/` by feature. Keep all 11 mapper files in the flat `presentation.mapper` package.

**Why:** `UiErrorMapper` and `CriteriaMapper` are cross-feature infra adapters with no clean feature home. Count is below the ~15 threshold for splitting.

**When to revisit:** If mapper count exceeds ~15, the only clean split is `entity/` vs `infra/`.

## Architectural Decision: `data/mapper/` Stays Flat

**Decision (confirmed):** Do not reorganize `data/mapper/` by source or by feature. Keep all 9 mapper files in the flat `data.mapper` package.

**Why:** Both reorganization axes fail:
- **By source** (`network/`, `firebase/`, `local/`) — `ChapterPagesMapper` spans Network + Room; `UserMapper` spans FirebaseAuth + Firestore. No clean home exists for either.
- **By feature** (`manga/`, `category/`, `user/`, `settings/`) — `ApiParamMapper` and `ExceptionMapper` are cross-feature infra adapters with no clean home.

**When to revisit:** If the mapper count grows above ~15, the only clean split is `entity/` vs `infra/` — separating the 7 domain-entity mappers from the 2 infrastructure adapters (`ApiParamMapper`, `ExceptionMapper`). Do not split by source or feature.

## Important Context

- **Package taxonomy (final — all layers):** `domain/`, `presentation/model/` both use `manga / category / user / settings` as top-level groups. Layers are structurally consistent end-to-end.
- **`ReadingHistoryList.kt` and `SuggestionList.kt`:** Both use `"${id}_$index"` compound keys. This is acceptable — these lists are replaced wholesale (not mutated), so index drift does not occur. Not a bug.
- **Audit verdict:** The deep-dive skill audit found zero remaining issues across: unstable collection types, missing `remember`/`derivedStateOf`, lambda stability, ViewModel injection anti-patterns, lazy list keys, `rememberSaveable` safety, and `@Stable`/`@Immutable` annotations.
- **`presentation/model/` untouched sub-packages:** `criteria/sort/`, `criteria/filter/`, and `error/` were already grouped and are correct. Not changing.
- **1-file rule:** Secondary sub-packages (2nd level within a group) only created when they hold ≥2 files. Top-level domain groups (manga, category, user, settings) kept regardless of file count.
- **Import updates (prior session):** All import changes were applied via a Python regex script using `re.MULTILINE` exact-line matching to avoid partial-string collisions.

## Files Modified

**This session (Compose performance fixes):**
- `presentation/screens/common/lists/manga/VerticalGridMangaList.kt` — `itemsIndexed` → `items` with stable key
- `presentation/screens/categories/CategoriesUiState.kt` — `Map` → `ImmutableMap`, `emptyMap()` → `persistentMapOf()`
- `presentation/screens/categories/CategoriesViewModel.kt` — added `.toImmutableMap()` + import
- `presentation/screens/reader/ReaderScreen.kt` — `remember(chapterPagesUiState)` wrapper + `remember` import
- `presentation/screens/common/menu/MenuDrawer.kt` — extracted `stringResource` calls + `remember(...)` wrapper
- `presentation/screens/common/lists/manga/MangaItem.kt` — `remember(manga.id)` lambda memoization

**Prior session (model package reorganisation — ~70 files):**
- `presentation/model/manga/` — `MangaUiModel.kt`, `ChapterUiModel.kt`, `ChapterPagesUiModel.kt`, `MangaLanguageUiModel.kt`
- `presentation/model/category/` — `CategoryUiModel.kt`, `CategoryTypeUiModel.kt`
- `presentation/model/user/` — `UserUiModel.kt`, `ReadingHistoryUiModel.kt`
- `presentation/model/settings/` — `ThemeModeUiModel.kt`
- All `presentation/mapper/`, `presentation/screens/*/ViewModel.kt`, `*UiState*.kt`, composable, and top-level presentation files updated for imports
