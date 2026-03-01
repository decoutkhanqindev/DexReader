# DexReader — Refactor Progress

## Overall Goal
Full Clean Architecture refactor: domain models own their types, mappers are pure converters, ViewModels are the domain↔presentation boundary, composables are pure receivers with no business logic.

---

## Phases

### Phase 0 — Structural Reorganization ✅ DONE
- Renamed + relocated Room and Moshi adapters
- Renamed util classes to `parser` package
- Moved `NetworkInterceptor` to centralized package
- Reorganized network DTOs into `mangadex_api` and `firebase` sub-packages
- Added Firestore collection/field constants for all Firestore access
- Moved `NavHostController` initialization to `NavGraph` composable

### Phase 1 — Domain Enum Wiring ✅ DONE
All 5 domain enums (`MangaLanguage`, `MangaSortCriteria`, `MangaSortOrder`, `MangaStatusFilter`, `MangaContentRatingFilter`) wired end-to-end through all layers.

**Data layer (`ParamMapper.kt`):**
- `MangaLanguage.toParam()` / `MangaStatusFilter.toParam()` / etc. — domain → API query param
- `String.toMangaLanguage()` — ISO code string → domain enum (data layer only)

**Presentation layer (`LanguageMapper.kt`, `CriteriaMapper.kt`):**
- `MangaLanguage ↔ MangaLanguageName` via `valueOf(name)`
- `*Option ↔ domain enum` via `valueOf(name)` for all 4 criteria enums

**`Chapter.translatedLanguage`** changed from `String` → `MangaLanguage`

**Remaining minor item:** Delete `MangaLanguageCodeParam` (only used internally in `ParamMapper`)

### Phase 2 — Presentation Enum Migration ✅ DONE
Replaced all stringly-typed and sealed-class criteria with proper presentation-layer enums.

**`MangaLanguageName` (`presentation/model/`):**
- Pure enum with `@StringRes val value: Int`
- `LanguageMapper.kt` handles `MangaLanguage ↔ MangaLanguageName`
- Deleted: `util/LanguageCodec.kt`

**Criteria Option enums (`presentation/model/criteria/sort/` and `criteria/filter/`):**
- `MangaSortCriteriaOption`, `MangaSortOrderOption` — in `criteria/sort/`
- `MangaStatusFilterOption`, `MangaContentRatingFilterOption` — in `criteria/filter/`
- All have `@StringRes val nameRes: Int`; composables use `option.nameRes` directly
- `CriteriaMapper.kt` handles `*Option → domain enum` via `valueOf(name)`
- `CategoryDetailsViewModel` is the single conversion point
- `CategoryDetailsCriteriaUiState` holds only Option enums
- Generic composables: `FilterCriteriaItem<T>`, `FilterValueOptions<T>` with `nameResOf: (T) -> Int`
- Deleted: `util/CriteriaCodec.kt`, `CategoryDetailsCriteria.kt`

### Phase 3 — Mapper Objects ✅ DONE
All 8 data layer mapper files (`CategoryMapper`, `ChapterMapper`, `ChapterPagesMapper`, `MangaMapper`, `ParamMapper`, `FavoriteMangaMapper`, `ReadingHistoryMapper`, `UserMapper`) wrapped in `object` singletons.

All call sites updated to use qualified static imports: `import ObjectName.functionName`.

### Phase 4 — Domain Model Default Constants ✅ DONE
Fallback values extracted from mapper raw string literals into domain model `companion object` constants.

| Model | Constants |
|---|---|
| `Manga` | `DEFAULT_TITLE`, `DEFAULT_DESCRIPTION`, `DEFAULT_AUTHOR`, `DEFAULT_ARTIST`, `DEFAULT_STATUS`, `DEFAULT_YEAR`, `DEFAULT_LAST_CHAPTER`, `DEFAULT_LAST_UPDATED` |
| `Chapter` | `DEFAULT_MANGA_ID`, `DEFAULT_TITLE`, `DEFAULT_CHAPTER_NUMBER`, `DEFAULT_VOLUME`, `DEFAULT_LANGUAGE` |
| `Category` | `DEFAULT_TITLE`, `DEFAULT_GROUP` |
| `ChapterPages` | `DEFAULT_BASE_URL`, `DEFAULT_HASH` |
| `FavoriteManga` | `DEFAULT_ADDED_AT` |
| `User` | `DEFAULT_NAME`, `DEFAULT_EMAIL` |

All 6 affected mapper files reference `ModelName.DEFAULT_*` — no more raw fallback literals.

### Phase 5 — Domain Model Property Renames ✅ DONE
All 11 properties across 6 domain models renamed to remove DTO/API naming leakage:

| Model | Old → New |
|---|---|
| `Chapter` | `publishAt` → `publishedAt`, `translatedLanguage` → `language`, `chapterNumber` → `number` |
| `Manga` | `availableTranslatedLanguages` → `availableLanguages`, `lastUpdated` → `updatedAt`, `lastChapter` → `latestChapter` |
| `ChapterPages` | `chapterDataHash` → `dataHash`, `pageUrls` → `pages` |
| `Category` | `group` → `type` |
| `ReadingHistory` | `totalChapterPages` → `pageCount` |
| `User` | `profilePictureUrl` → `avatarUrl` |

All call sites updated across data mappers, use cases, ViewModels, and composables.

**Remaining minor cleanup (deferred):**
- [ ] Delete `MangaLanguageCodeParam` — replace usages in `ParamMapper` with inline ISO string constants

### Phase 6 — ViewModel Business Logic Extraction 🔲 TODO
Extract remaining business logic from ViewModels into Use Cases. See `memory/refactoring-gaps.md` for full inventory.

Known gaps:
- Filtering/pagination state logic in `CategoryDetailsViewModel`
- Reading progress calculations in `ReaderViewModel`
- Any direct repository calls in VMs that bypass use cases

---

## Architecture Decisions Log

### Decision: `valueOf(name)` for cross-layer enum mapping
**Why:** Presentation Option enum entry names mirror domain enum entry names exactly. This makes mapping a zero-cost name lookup with no `when` expression. Enforced by convention — entry names must stay in sync.

### Decision: Option enums own their `@StringRes`
**Why:** String resource IDs belong in the presentation layer. Putting them in the enum constructor means composables never need a `when` block for display — just `stringResource(option.nameRes)`. Section title strings that aren't per-option remain as direct `R.string.*` references.

### Decision: ViewModel as the domain↔presentation boundary
**Why:** Composables should be pure, stateless receivers. All `Option → domain enum` conversion happens in the ViewModel before calling use cases. Composables never import domain layer types.

### Decision: Mapper `object` singletons (not companion objects, not top-level)
**Why:** `object` is the idiomatic Kotlin singleton. It keeps mappers stateless, testable, and namespaced without needing a class instance. Call sites use static extension imports: `import ObjectName.functionName`.

### Decision: Domain model companion constants for defaults
**Why:** Fallback values are domain knowledge ("Untitled", "Unknown", `MangaLanguage.ENGLISH`). Scattering them as raw literals across mapper files violates single-source-of-truth. Domain models own their semantic defaults; mappers are pure converters.

### Decision: FavoriteManga.toManga() empty strings are intentional
**Why:** FavoriteManga doesn't store description, artist, year, etc. Those fields are explicitly `""` — not fallback defaults. They must NOT be replaced with `Manga.DEFAULT_*` (which have different semantic values like `"No description ..."`).

### Decision: `rememberSaveable` enum state with custom `Saver`
**Why:** Enum values aren't Parcelable by default. Using `Saver(save = { it.name }, restore = { Enum.valueOf(it) })` and `listSaver(...)` for lists keeps state across process death without adding `@Parcelize` to presentation enums.
