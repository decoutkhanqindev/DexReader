# DexReader — Presentation Layer Domain Isolation Progress

## Plan Overview

Goal: Every `presentation/` type ends in `UiModel` or `UiError`. ViewModels are the translation
boundary — they consume domain types from use cases and emit presentation types to composables. No
composable imports from `domain.*`.

---

## Phase Status

| Batch       | Description                               | Status | Notes                                             |
|-------------|-------------------------------------------|--------|---------------------------------------------------|
| **Batch 0** | Rename existing presentation types        | ✅ DONE | All types renamed to UiModel/UiError              |
| **Batch 1** | Create new UiModel data classes + mappers | ✅ DONE | 12 new files                                      |
| **Batch 2** | Category domain isolation                 | ✅ DONE | `Map<CategoryTypeUiModel, List<CategoryUiModel>>` |
| **Batch 3** | User domain isolation                     | ✅ DONE | `UserViewModel` emits `StateFlow<UserUiModel?>`   |
| **Batch 4** | Manga domain isolation                    | ✅ DONE | CA violation in FavoritesContent fixed            |
| **Batch 5** | Chapter domain isolation                  | ✅ DONE | `startedChapterId: StateFlow<String?>`            |
| **Batch 6** | ReadingHistory domain isolation           | ✅ DONE | Private domain list + mapped public StateFlow     |
| **Batch 7** | ChapterPages (Reader) isolation           | ✅ DONE | `pageImageUrls` pre-computed in mapper            |

**Build:** `./gradlew :app:compileDebugKotlin` → `BUILD SUCCESSFUL`, 0 errors ✅

---

## Batch 0 — Rename Existing Presentation Types ✅ DONE

### Type renames

| Old                              | New                               |
|----------------------------------|-----------------------------------|
| `MangaLanguageName`              | `MangaLanguageUiModel`            |
| `CategoryTypeOption`             | `CategoryTypeUiModel`             |
| `ThemeOption`                    | `ThemeUiModel`                    |
| `MangaStatusFilterOption`        | `MangaStatusFilterUiModel`        |
| `MangaContentRatingFilterOption` | `MangaContentRatingFilterUiModel` |
| `MangaSortCriteriaOption`        | `MangaSortCriteriaUiModel`        |
| `MangaSortOrderOption`           | `MangaSortOrderUiModel`           |
| `FeatureError`                   | `FeatureUiError`                  |
| `UserError`                      | `UserUiError`                     |

### Mapper method renames

| Old                     | New                        |
|-------------------------|----------------------------|
| `toMangaLanguageName()` | `toMangaLanguageUiModel()` |
| `toFeatureError()`      | `toFeatureUiError()`       |
| `toUserError()`         | `toUserUiError()`          |
| `toThemeOption()`       | `toThemeUiModel()`         |

---

## Batch 1 — New UiModel Files ✅ DONE

### New model files

| File                             | Key fields                                                                                                                                                                                 |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `model/MangaUiModel.kt`          | id, title, coverUrl, description, author, artist, categories(List\<CategoryUiModel\>), status(pre-cased), year, availableLanguages(List\<MangaLanguageUiModel\>), latestChapter, updatedAt |
| `model/ChapterUiModel.kt`        | id, mangaId, title, number, volume, publishedAt                                                                                                                                            |
| `model/ChapterPagesUiModel.kt`   | chapterId, pageImageUrls(precomputed), totalPages                                                                                                                                          |
| `model/ReadingHistoryUiModel.kt` | id, mangaId, mangaTitle, mangaCoverUrl, chapterId, chapterTitle, chapterNumber, chapterVolume, lastReadPage, pageCount, lastReadAt                                                         |
| `model/CategoryUiModel.kt`       | id, title                                                                                                                                                                                  |
| `model/UserUiModel.kt`           | id, name, email, avatarUrl                                                                                                                                                                 |

### New mapper files

| File                               | Functions                                                                               |
|------------------------------------|-----------------------------------------------------------------------------------------|
| `mapper/MangaUiMapper.kt`          | `Manga.toMangaUiModel()`, `FavoriteManga.toMangaUiModel()`                              |
| `mapper/ChapterUiMapper.kt`        | `Chapter.toChapterUiModel()`                                                            |
| `mapper/ChapterPagesUiMapper.kt`   | `ChapterPages.toChapterPagesUiModel()` — pre-computes `"$baseUrl/data/$dataHash/$page"` |
| `mapper/ReadingHistoryUiMapper.kt` | `ReadingHistory.toReadingHistoryUiModel()`                                              |
| `mapper/CategoryUiMapper.kt`       | `Category.toCategoryUiModel()`                                                          |
| `mapper/UserUiMapper.kt`           | `User.toUserUiModel()` + `UserUiModel.toDomainUser()` (reverse mapper)                  |

---

## Batches 2–7 ✅ ALL DONE

### Key implementation decisions per batch

**Batch 2 (Category):** `CategoriesUiState` → `Map<CategoryTypeUiModel, List<CategoryUiModel>>`. VM
maps with `toCategoryUiModel()`.

**Batch 3 (User):**

- `UserViewModel`: `_domainUserProfile: MutableStateFlow<User?>` private;
  `userProfile: StateFlow<UserUiModel?>` via `.map { }.stateIn(Eagerly)`
- `ProfileViewModel.updateCurrentUser(UserUiModel?)`: stores domain via `toDomainUser()` for use
  case
- All nav/menu/screen composables: `currentUser: UserUiModel?`

**Batch 4 (Manga):**

- `MangaDetailsViewModel` keeps `private val _domainManga: MutableStateFlow<Manga?>` for
  `addToFavoritesUseCase`
- `FavoritesContent` CA violation fixed: removed `import data.mapper.FavoriteMangaMapper.toManga`
- `FavoriteMangaMapper.toManga()` function deleted
- Status pre-formatting (`replaceFirstChar`) moved from composables into `MangaUiMapper`

**Batch 5 (Chapter):**

- `startedChapter: StateFlow<Chapter?>` → `startedChapterId: StateFlow<String?>` (composables only
  need ID)
- `ActionButtonsSection` receives `startedChapterId: String?` + `mangaId: String`

**Batch 6 (ReadingHistory):**

- `_readingHistoryList: MutableStateFlow<List<ReadingHistory>>` kept private in
  `MangaDetailsViewModel` for `findContinueTarget()` domain logic
- Public `readingHistoryList: StateFlow<List<ReadingHistoryUiModel>>` exposed via
  `.map { list -> list.map { it.toReadingHistoryUiModel() } }.stateIn(...)`
- `continueChapter: StateFlow<ReadingHistoryUiModel?>` mapped from private list

**Batch 7 (ChapterPages):**

- `ChapterPagesUiState.Success.chapterPages: ChapterPagesUiModel`
- `ReaderContent` uses `pageImageUrls` (precomputed); URL construction removed from composable

---

## Architecture Decisions Log

1. **Naming convention**: `UiModel` suffix for all data/enum presentation models; `UiError` suffix
   for all error sealed classes.

2. **Translation boundary**: ViewModels map domain → UiModel. Composables only see UiModel types. No
   `domain.*` imports in composables.

3. **Private domain retention in VMs**: When a VM needs the domain type for use case calls AND
   UiModel for state, keep a private domain field. Three examples:
    - `MangaDetailsViewModel._domainManga` for `addToFavoritesUseCase`
    - `MangaDetailsViewModel._readingHistoryList` for `findContinueTarget()` domain logic
    - `ProfileViewModel.currentDomainUser` for `UpdateUserProfileUseCase`
    - `UserViewModel._domainUserProfile` for observing from use case before mapping

4. **`startedChapterId` pattern**: Only the chapter ID exposed from VM, not the full `Chapter?`
   object.

5. **Status formatting**: `status.replaceFirstChar` moved into `MangaUiMapper`.
   `MangaUiModel.status` is always pre-formatted.

6. **`ChapterPagesUiModel.pageImageUrls`**: URL construction moved to `ChapterPagesUiMapper`.
   Composables receive ready-to-use URL strings.

7. **`FavoriteManga.toManga()` deletion**: The only consumer was a composable (CA violation).
   Removed the bridge function entirely.

8. **Reverse mapper**: `UserUiModel.toDomainUser()` in `UserUiMapper` — the only UI→domain mapper in
   the presentation layer. Justified because `UpdateUserProfileUseCase` requires domain `User`.

9. **Domain exceptions**: Actual code uses `InfrastructureException`, `BusinessException`,
   `ValidationException` — NOT `RemoteException`/`MangaException` as documented in older memory
   notes.
