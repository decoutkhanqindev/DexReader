# DexReader - Claude Code Session Guide

## Status
**Phase:** Clean Architecture Refactor — Domain Model Defaults + Mapper Objects
**Overall:** ~70% through the full refactor plan. All mapper, enum, and domain-default work is complete. Remaining gaps are VM business logic extraction and one type fix.

---

## Completed This Session

- **CategoryDetailsCriteria enum migration** — replaced sealed class (`SortCriteria`, `SortOrder`, `FilterValue`) with 4 pure presentation Option enums:
  - `presentation/model/criteria/sort/MangaSortCriteriaOption.kt`
  - `presentation/model/criteria/sort/MangaSortOrderOption.kt`
  - `presentation/model/criteria/filter/MangaStatusFilterOption.kt`
  - `presentation/model/criteria/filter/MangaContentRatingFilterOption.kt`
  - All have `@StringRes val nameRes: Int` — composables use `option.nameRes` directly
- **`presentation/mapper/CriteriaMapper.kt`** — maps Option enums → domain enums via `valueOf(name)`
- **`CategoryDetailsViewModel`** — all domain↔presentation conversion moved here; composables receive only Option enums
- **`CategoryDetailsCriteriaUiState`** — holds 4 Option enum fields (not domain enums)
- **Sort/Filter composables** made generic — `FilterCriteriaItem<T>`, `FilterValueOptions<T>` with `nameResOf: (T) -> Int`
- **`rememberSaveable` Savers** for enum state: `Saver(save = { it.name }, restore = { Enum.valueOf(it) })`
- **Deleted** `util/CriteriaCodec.kt` and `CategoryDetailsCriteria.kt`
- **All mapper functions wrapped in `object`** — 8 data layer mappers now singleton objects; call sites use `ObjectName.functionName` qualified static imports
- **Domain model companion constants** — fallback defaults moved from mapper raw literals into domain models:
  - `Manga`, `Chapter`, `Category`, `ChapterPages`, `FavoriteManga`, `User` all have `companion object { const val DEFAULT_* }`
  - All 6 affected mapper files updated to reference `ModelName.DEFAULT_*`

---

## Next Session - Start Here

**First step:** Open `app/src/main/java/com/decoutkhanqindev/dexreader/domain/model/Manga.kt`

**Task:** Fix `availableTranslatedLanguages: List<String>` → `List<MangaLanguage>`
- This is the last remaining type gap from Phase 1 (domain enum wiring)
- Requires updating: `MangaMapper.kt` (already calls `.toMangaLanguage()` — just remove the intermediate `String` storage), anywhere `availableTranslatedLanguages` is consumed in presentation
- Then check `MangaDetailsViewModel` and `MangaDetailsScreen` for how the list is displayed

**After that:**
- Delete `MangaLanguageCodeParam` enum (still used only inside `ParamMapper` — replace with raw ISO strings or inline constants)
- Address remaining VM business logic gaps (see `refactoring-gaps.md` in memory dir)

---

## Important Context

### Enum naming convention (critical)
Option enum entry names **must exactly match** their domain enum entry names — this enables safe `valueOf(name)` mapping with zero when-expressions. Never rename entries in isolation.

### Mapper object pattern
All mappers are now `object` singletons. Cross-object calls use qualified static imports:
```kotlin
import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
```

### Domain defaults vs intentional empties
In `FavoriteMangaMapper.toManga()`, fields like `description = ""`, `artist = ""`, `year = ""` are **intentional** (FavoriteManga doesn't store those fields) — NOT fallback defaults. Do not replace them with `Manga.DEFAULT_*` which have different semantic values (e.g. `"No description ..."`).

### String resources location
- Per-option display strings → live in the Option enum constructor (`nameRes: Int`)
- Section header strings (e.g. `R.string.filter_status`) → remain as direct references in the composable that uses them

### Layer boundaries
- `LanguageCodec.kt` is deleted — display name logic now in `presentation/mapper/LanguageMapper.kt`
- `CriteriaCodec.kt` is deleted — criteria codec logic now in `presentation/mapper/CriteriaMapper.kt`
- `ParamMapper.kt` (data layer) owns all ISO code strings and API param strings

---

## Files Modified This Session

### New files
- `presentation/model/criteria/sort/MangaSortCriteriaOption.kt`
- `presentation/model/criteria/sort/MangaSortOrderOption.kt`
- `presentation/model/criteria/filter/MangaStatusFilterOption.kt`
- `presentation/model/criteria/filter/MangaContentRatingFilterOption.kt`
- `presentation/mapper/CriteriaMapper.kt`

### Deleted files
- `util/CriteriaCodec.kt`
- `presentation/screens/category_details/CategoryDetailsCriteria.kt`

### Domain models (companion constants added)
- `domain/model/Manga.kt`
- `domain/model/Chapter.kt`
- `domain/model/Category.kt`
- `domain/model/ChapterPages.kt`
- `domain/model/FavoriteManga.kt`
- `domain/model/User.kt`

### Data mappers (object wrapping + domain constant refs)
- `data/mapper/MangaMapper.kt`
- `data/mapper/ChapterMapper.kt`
- `data/mapper/CategoryMapper.kt`
- `data/mapper/ChapterPagesMapper.kt`
- `data/mapper/FavoriteMangaMapper.kt`
- `data/mapper/UserMapper.kt`
- `data/mapper/ReadingHistoryMapper.kt`
- `data/mapper/ParamMapper.kt`

### Repositories (updated for object mapper imports)
- `data/repository/CategoryRepositoryImpl.kt`
- `data/repository/ChapterRepositoryImpl.kt`
- `data/repository/MangaRepositoryImpl.kt`
- `data/repository/CacheRepositoryImpl.kt`
- `data/repository/FavoritesRepositoryImpl.kt`
- `data/repository/HistoryRepositoryImpl.kt`
- `data/repository/UserRepositoryImpl.kt`

### Presentation (criteria composables + sort/filter sheets)
- `presentation/screens/category_details/CategoryDetailsCriteriaUiState.kt`
- `presentation/screens/category_details/CategoryDetailsViewModel.kt`
- `presentation/screens/category_details/components/SortBottomSheet.kt`
- `presentation/screens/category_details/components/FilterBottomSheet.kt`
- `presentation/screens/category_details/components/SortCriteriaItem.kt`
- `presentation/screens/category_details/components/SortOrderOptions.kt`
- `presentation/screens/category_details/components/VerticalGridSortCriteriaList.kt`
- `presentation/screens/category_details/components/FilterCriteriaItem.kt`
- `presentation/screens/category_details/components/FilterValueOptions.kt`
- `presentation/screens/category_details/components/VerticalGridFilterCriteriaList.kt`
- `presentation/screens/category_details/CategoryDetailsContent.kt`
- `presentation/screens/category_details/CategoryDetailsScreen.kt`
- `presentation/screens/favorites/components/FavoritesContent.kt`

### Resources
- `app/src/main/res/values/strings.xml` (added sort/filter/status/content-rating strings)
