# DexReader — Refactoring Progress

## Phase Overview

| # | Phase | Status |
|---|---|---|
| 1 | Domain Enums — replace raw strings with typed enums end-to-end | ✅ done |
| 2 | Domain Model Property Renames — domain-meaningful names | ✅ done |
| 3 | Domain Model Default Constants — companion object fallbacks | ✅ done |
| 4 | Mapper Object Pattern — wrap all data mappers in `object` | ✅ done |
| 5 | CategoryGroup → CategoryType — sealed class → domain enum | ✅ done |
| 6 | CA Fix: UseCase Grouping — move client-side transforms out of VMs | ✅ done (categories) |
| 7 | Remaining CA Gaps — business logic still in other ViewModels | 🔲 todo |
| 8 | MangaLanguageCodeParam cleanup | 🔲 todo |

---

## Phase Detail

### Phase 1 — Domain Enums ✅
All 5 domain enums wired end-to-end across all three layers:

| Domain enum | Presentation Option enum | Mapper |
|---|---|---|
| `MangaLanguage` | `MangaLanguageName` (@StringRes) | `LanguageMapper` |
| `MangaSortCriteria` | `MangaSortCriteriaOption` | `CriteriaMapper` |
| `MangaSortOrder` | `MangaSortOrderOption` | `CriteriaMapper` |
| `MangaStatusFilter` | `MangaStatusFilterOption` | `CriteriaMapper` |
| `MangaContentRatingFilter` | `MangaContentRatingFilterOption` | `CriteriaMapper` |

- ISO/API strings live only in `data/` (`ParamMapper`, `ChapterMapper`)
- `@StringRes` lives only in `presentation/model/`
- Mapper objects in `presentation/mapper/` use `valueOf(this.name)` — enum name identity across layers
- `rememberSaveable` for enum state: `Saver(save = { it.name }, restore = { Enum.valueOf(it) })`

### Phase 2 — Domain Model Property Renames ✅

| Model | Old → New |
|---|---|
| `Chapter` | `publishAt` → `publishedAt`, `translatedLanguage` → `language`, `chapterNumber` → `number` |
| `Manga` | `availableTranslatedLanguages` → `availableLanguages`, `lastUpdated` → `updatedAt`, `lastChapter` → `latestChapter` |
| `ChapterPages` | `chapterDataHash` → `dataHash`, `pageUrls` → `pages` |
| `Category` | `group` → `type` |
| `ReadingHistory` | `totalChapterPages` → `pageCount` |
| `User` | `profilePictureUrl` → `avatarUrl` |

- `ChapterCacheEntity.chapterDataHash` → Kotlin property `dataHash`; `@ColumnInfo(name = "chapterDataHash")` kept for Room DB column compat
- Firebase DTOs kept as-is — mapper is the boundary

### Phase 3 — Domain Model Default Constants ✅
All domain models own fallback values as `companion object` constants:
- `Manga` — `DEFAULT_TITLE/DESCRIPTION/AUTHOR/ARTIST/STATUS/YEAR/LAST_CHAPTER/LAST_UPDATED`
- `Chapter` — `DEFAULT_MANGA_ID/TITLE/CHAPTER_NUMBER/VOLUME/LANGUAGE`
- `Category` — `DEFAULT_TITLE`, `DEFAULT_TYPE` (= `CategoryType.UNKNOWN`)
- `ChapterPages` — `DEFAULT_BASE_URL/HASH`
- `FavoriteManga` — `DEFAULT_ADDED_AT`
- `User` — `DEFAULT_NAME/EMAIL`

### Phase 4 — Mapper Object Pattern ✅
All 8 data layer mappers wrapped in `object`:
`CategoryMapper`, `ChapterMapper`, `ChapterPagesMapper`, `MangaMapper`, `ParamMapper`, `FavoriteMangaMapper`, `ReadingHistoryMapper`, `UserMapper`

Call sites use qualified static import: `import ObjectName.functionName`

### Phase 5 — CategoryGroup → CategoryType ✅
- `CategoryGroup` sealed class deleted
- `domain/model/CategoryType.kt` — `GENRE, THEME, FORMAT, CONTENT, UNKNOWN`
- `presentation/model/CategoryTypeOption.kt` — `@StringRes val nameRes: Int`
- `presentation/mapper/CategoryTypeMapper.kt` — `CategoryType.toCategoryTypeOption()` (domain → presentation only)
- `Category.type: String` → `Category.type: CategoryType`
- `CategoryMapper` — private `String.toCategoryType()` via case-insensitive `entries.firstOrNull`
- Components renamed: `CategoryGroupSection` → `CategoryTypeSection`, `CategoryGroupHeader` → `CategoryTypeHeader`
- 5 string resources added

### Phase 6 — CA Fix: UseCase Grouping ✅ (categories screen)
**Problem:** `CategoriesViewModel` was doing `categoryList.filter { it.type == CategoryType.GENRE }` × 4 — client-side filter belongs in UseCase per domain layer guidelines.

**Fix:**
- `GetCategoryListUseCase` now returns `Result<Map<CategoryType, List<Category>>>` with `groupBy { it.type }` inside
- `CategoriesUiState.Success`: 4 named lists → `Map<CategoryTypeOption, List<Category>> categoryMap`
- `CategoriesViewModel`: converts domain map → presentation map using `CategoryTypeOption.entries.associateWith { option -> grouped[CategoryType.valueOf(option.name)] ?: emptyList() }`
- `CategoriesContent`: 4 hardcoded `item {}` → single dynamic `items(categoryMap.keys.toList())`

### Phase 7 — Remaining CA Gaps 🔲
See `memory/refactoring-gaps.md` for full list of business logic still in ViewModels.

Known items:
- Pagination heuristic (`hasNextPage = list.size >= PAGE_SIZE`) in `CategoryDetailsViewModel` — borderline, low priority
- Other VMs with inline business logic (TBD from gaps file)

### Phase 8 — MangaLanguageCodeParam cleanup 🔲
`MangaLanguageCodeParam` still used internally in `ParamMapper` and `CategoryMapper` (ENGLISH key lookup). Can be replaced with a direct string constant once the team agrees on the approach.

---

## Architecture Decisions Log

| Decision | Rationale |
|---|---|
| Domain enums have zero Android/framework imports | Domain layer must be pure Kotlin |
| Presentation Option enums hold `@StringRes` | Only presentation layer can reference Android resources |
| Mapper objects use `valueOf(this.name)` | Enum entry names are kept identical across layers — no lookup table needed |
| `toCategoryType()` mapper NOT created | Inverse direction (presentation → domain) not needed for category type; inline `CategoryType.valueOf(option.name)` used in ViewModel |
| `GetCategoryListUseCase` returns grouped map | Client-side `filter {}` belongs in UseCase per domain guidelines (Gray Area rule) |
| `CategoriesUiState.Success` uses `Map<CategoryTypeOption, List<Category>>` | Presentation map key = presentation type; ViewModel is the domain→presentation boundary |
| `CategoriesContent` iterates map keys dynamically | Decoupled from hardcoded 4-section assumption; if API drops a type, no crash |
| `ChapterCacheEntity` column name kept as `chapterDataHash` | Changing Room column name requires a DB migration; Kotlin property renamed, `@ColumnInfo` preserved |
| Firebase DTOs not renamed | They're at the network boundary; mapper is the adapter |
