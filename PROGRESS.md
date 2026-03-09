# DexReader — Presentation Layer Domain Isolation Progress

## Plan Overview

Goal: Every `presentation/` type ends in `UiModel` or `UiError`. ViewModels are the translation boundary — they consume domain types from use cases and emit presentation types to composables. No composable imports from `domain.*`.

---

## Phase Status

| Batch | Description | Status | Notes |
|---|---|---|---|
| **Batch 0** | Rename existing presentation types | 🔶 IN PROGRESS (~60%) | 18 files still need old names replaced |
| **Batch 1** | Create new UiModel data classes + mappers | ✅ DONE | 12 new files created |
| **Batch 2** | Category domain isolation | ⬜ TODO | Depends on Batch 0 complete |
| **Batch 3** | User domain isolation | ⬜ TODO | Depends on Batch 0 complete |
| **Batch 4** | Manga domain isolation | ⬜ TODO | Depends on Batches 0+1+2 |
| **Batch 5** | Chapter domain isolation | ⬜ TODO | Depends on Batches 0+1 |
| **Batch 6** | ReadingHistory domain isolation | ⬜ TODO | Depends on Batches 0+1 |
| **Batch 7** | ChapterPages (Reader) isolation | ⬜ TODO | Depends on Batches 0+1 |

---

## Batch 0 — Rename Existing Presentation Types

### Type renames
| Old | New | File status |
|---|---|---|
| `MangaLanguageName` | `MangaLanguageUiModel` | ✅ New file created, old deleted |
| `CategoryTypeOption` | `CategoryTypeUiModel` | ✅ New file created, old deleted |
| `ThemeOption` | `ThemeUiModel` | ✅ New file created, old deleted |
| `MangaStatusFilterOption` | `MangaStatusFilterUiModel` | ✅ New file created, old deleted |
| `MangaContentRatingFilterOption` | `MangaContentRatingFilterUiModel` | ✅ New file created, old deleted |
| `MangaSortCriteriaOption` | `MangaSortCriteriaUiModel` | ✅ New file created, old deleted |
| `MangaSortOrderOption` | `MangaSortOrderUiModel` | ✅ New file created, old deleted |
| `FeatureError` | `FeatureUiError` | ✅ New file created, old deleted |
| `UserError` | `UserUiError` | ✅ New file created, old deleted |

### Mapper method renames
| Old | New | Status |
|---|---|---|
| `toMangaLanguageName()` | `toMangaLanguageUiModel()` | ✅ Done |
| `toFeatureError()` | `toFeatureUiError()` | ✅ Done |
| `toUserError()` | `toUserUiError()` | ✅ Done |
| `toThemeOption()` | `toThemeUiModel()` | ✅ Done |

### Consumer files updated (Batch 0)
| File | Status |
|---|---|
| `presentation/mapper/LanguageMapper.kt` | ✅ |
| `presentation/mapper/ErrorMapper.kt` | ✅ |
| `presentation/mapper/ThemeMapper.kt` | ✅ |
| `presentation/mapper/CriteriaMapper.kt` | ✅ |
| `screens/auth/EmailInputField.kt` | ✅ |
| `screens/auth/NameInputField.kt` | ✅ |
| `screens/auth/PasswordInputField.kt` | ✅ |
| `screens/auth/forgot_password/ForgotPasswordUiState.kt` | ✅ |
| `screens/auth/forgot_password/ForgotPasswordViewModel.kt` | ✅ |
| `screens/auth/forgot_password/components/ForgotPasswordContent.kt` | ✅ |
| `screens/auth/forgot_password/components/ForgotPasswordForm.kt` | ✅ |
| `screens/auth/login/LoginUiState.kt` | ✅ |
| `screens/auth/login/LoginViewModel.kt` | ✅ |
| `screens/auth/login/components/LoginContent.kt` | ✅ |
| `screens/auth/login/components/LoginForm.kt` | ✅ |
| `screens/auth/register/RegisterUiState.kt` | ✅ |
| `screens/auth/register/RegisterViewModel.kt` | ✅ |
| `screens/auth/register/components/RegisterContent.kt` | ✅ |
| `screens/auth/register/components/RegisterForm.kt` | ✅ |
| `screens/categories/CategoriesUiState.kt` | ✅ |
| `screens/categories/CategoriesViewModel.kt` | ✅ |
| `screens/categories/CategoriesScreen.kt` | ✅ |
| `screens/categories/components/CategoriesContent.kt` | ✅ |
| `screens/categories/components/CategoryTypeHeader.kt` | ✅ |
| `screens/categories/components/CategoryTypeSection.kt` | ✅ |
| `screens/category_details/CategoryDetailsCriteriaUiState.kt` | ✅ |
| `screens/category_details/CategoryDetailsScreen.kt` | ✅ |
| `screens/category_details/CategoryDetailsViewModel.kt` | ✅ |
| `screens/category_details/components/CategoryDetailsContent.kt` | ✅ |
| `screens/category_details/components/filter/FilterBottomSheet.kt` | ✅ |
| `screens/category_details/components/filter/VerticalGridFilterCriteriaList.kt` | ✅ |
| `screens/category_details/components/sort/SortBottomSheet.kt` | ✅ |
| `screens/category_details/components/sort/SortCriteriaItem.kt` | ✅ |
| `screens/category_details/components/sort/SortOrderOptions.kt` | ✅ |
| `screens/category_details/components/sort/VerticalGridSortCriteriaList.kt` | ✅ |
| `screens/settings/SettingsUiState.kt` | ✅ |
| `screens/common/base/BasePaginationUiState.kt` | ✅ |
| `navigation/NavGraph.kt` | ✅ |
| `screens/home/HomeUiState.kt` | ✅ (FeatureError→FeatureUiError only) |
| `screens/manga_details/MangaDetailsUiState.kt` | ✅ (FeatureError→FeatureUiError only) |

### NOT YET UPDATED (Batch 0 incomplete — 18 files)
| File | What needs changing |
|---|---|
| `screens/manga_details/components/chapters/ChapterLanguageBottomSheet.kt` | `MangaLanguageName` → `MangaLanguageUiModel` |
| `screens/manga_details/components/chapters/MangaChaptersHeader.kt` | `MangaLanguageName` → `MangaLanguageUiModel` |
| `screens/manga_details/components/chapters/MangaChaptersSection.kt` | `MangaLanguageName` → `MangaLanguageUiModel` |
| `screens/manga_details/components/MangaDetailsContent.kt` | `MangaLanguageName` → `MangaLanguageUiModel` |
| `screens/manga_details/MangaDetailsViewModel.kt` | `MangaLanguageName` → `MangaLanguageUiModel`, `toMangaLanguageName` → `toMangaLanguageUiModel` |
| `screens/profile/components/ProfileContent.kt` | `UserError` → `UserUiError` (minor) |
| `screens/profile/ProfileUiState.kt` | `UserError` maybe? (check actual content) |
| `screens/profile/ProfileViewModel.kt` | check actual content |
| `screens/reader/ReaderUiState.kt` | `FeatureError` → `FeatureUiError` |
| `screens/reader/ReaderViewModel.kt` | `toFeatureError` → `toFeatureUiError` |
| `screens/search/SearchViewModel.kt` | `toFeatureError` → `toFeatureUiError` |
| `screens/search/SuggestionsUiState.kt` | `FeatureError` → `FeatureUiError` |
| `screens/settings/components/SettingsContent.kt` | `ThemeOption` → `ThemeUiModel` |
| `screens/settings/components/ThemeOptionItem.kt` | `ThemeOption` → `ThemeUiModel` |
| `screens/settings/components/ThemeOptionList.kt` | `ThemeOption` → `ThemeUiModel` |
| `screens/settings/SettingsScreen.kt` | `ThemeOption` → `ThemeUiModel` |
| `screens/settings/SettingsViewModel.kt` | `ThemeOption` → `ThemeUiModel`, `toThemeOption` → `toThemeUiModel` |
| `presentation/theme/Theme.kt` | `ThemeOption` → `ThemeUiModel` |

---

## Batch 1 — New UiModel Files ✅ DONE

### New model files (all `@Immutable data class`)
| File | Fields |
|---|---|
| `model/MangaUiModel.kt` | id, title, coverUrl, description, author, artist, categories(List\<CategoryUiModel\>), status(pre-cased), year, availableLanguages(List\<MangaLanguageUiModel\>), latestChapter, updatedAt |
| `model/ChapterUiModel.kt` | id, mangaId, title, number, volume, publishedAt |
| `model/ChapterPagesUiModel.kt` | chapterId, pageImageUrls(precomputed URLs), totalPages |
| `model/ReadingHistoryUiModel.kt` | id, mangaId, mangaTitle, mangaCoverUrl, chapterId, chapterTitle, chapterNumber, chapterVolume, lastReadPage, pageCount, lastReadAt |
| `model/CategoryUiModel.kt` | id, title |
| `model/UserUiModel.kt` | id, name, email, avatarUrl |

### New mapper files (all `object`)
| File | Functions |
|---|---|
| `mapper/MangaUiMapper.kt` | `Manga.toMangaUiModel()`, `FavoriteManga.toMangaUiModel()` |
| `mapper/ChapterUiMapper.kt` | `Chapter.toChapterUiModel()` |
| `mapper/ChapterPagesUiMapper.kt` | `ChapterPages.toChapterPagesUiModel()` — pre-computes `"$baseUrl/data/$dataHash/$page"` |
| `mapper/ReadingHistoryUiMapper.kt` | `ReadingHistory.toReadingHistoryUiModel()` |
| `mapper/CategoryUiMapper.kt` | `Category.toCategoryUiModel()` |
| `mapper/UserUiMapper.kt` | `User.toUserUiModel()` |

---

## Batches 2–7 — Domain Type Isolation (TODO)

### Batch 2 — Category
**Files:**
- `CategoriesUiState.kt`: `Map<CategoryTypeUiModel, List<Category>>` → `Map<CategoryTypeUiModel, List<CategoryUiModel>>`
- `CategoriesViewModel.kt`: add `.map { it.toCategoryUiModel() }` per list
- `CategoryItem.kt`: `Category` → `CategoryUiModel`
- `CategoryList.kt`: `List<Category>` → `List<CategoryUiModel>`
- `CategoryTypeSection.kt`: `List<Category>` → `List<CategoryUiModel>`

### Batch 3 — User
**Files:**
- `ProfileUiState.kt`: `currentUser: User?` → `currentUser: UserUiModel?`
- `ProfileViewModel.kt`:
  - Add `private var currentDomainUser: User? = null`
  - `updateCurrentUser(User?)` → sets `currentDomainUser` AND maps to `UserUiModel?` for state
  - `updateUserProfile()` → use `currentDomainUser` for use case call
- `ProfileContent.kt`: import-only change (same field names on `UserUiModel`)

### Batch 4 — Manga
**Files (changes):**
- `HomeUiState.kt`: `List<Manga>` → `List<MangaUiModel>`
- `MangaDetailsUiState.kt`: `Manga` → `MangaUiModel`
- `HomeViewModel.kt`: `.map { it.toMangaUiModel() }` on each list
- `CategoryDetailsViewModel.kt`: `BasePaginationUiState<Manga>` → `BasePaginationUiState<MangaUiModel>`, map results
- `MangaDetailsViewModel.kt`: add `private var _domainManga: Manga? = null`; set before mapping; use for `addToFavoritesUseCase`; simplify `availableLanguages` (just `state.manga.availableLanguages`)
- `FavoritesViewModel.kt`: `BasePaginationUiState<FavoriteManga>` → `BasePaginationUiState<MangaUiModel>`; map via `FavoriteManga.toMangaUiModel()`
- `SearchViewModel.kt`: `BasePaginationUiState<Manga>` → `BasePaginationUiState<MangaUiModel>`
- `MangaItem.kt`, `common/lists/manga/MangaInfo.kt`, `manga_details/info/MangaInfo.kt`, `MangaInfoSection.kt`, `MangaSummarySection.kt`: `Manga` → `MangaUiModel`; remove `.replaceFirstChar` (now in mapper)
- `MangaCategoryList.kt`, `MangaCategoryItem.kt`: `Category` → `CategoryUiModel`; remove `.replaceFirstChar`
- `HorizontalMangaList.kt`, `VerticalGridMangaList.kt`, `MangaListSection.kt`: `List<Manga>` → `List<MangaUiModel>`
- `ResultsSection.kt`, `CategoryDetailsContent.kt`: `BasePaginationUiState<Manga>` → `BasePaginationUiState<MangaUiModel>`
- `FavoritesContent.kt`: remove `data.mapper.FavoriteMangaMapper.toManga` import, remove `.map { it.toManga() }`, change state type to `BasePaginationUiState<MangaUiModel>`
- `data/mapper/FavoriteMangaMapper.kt`: **DELETE** `toManga()` function

### Batch 5 — Chapter
**Files:**
- `MangaDetailsViewModel.kt`: `BasePaginationUiState<Chapter>` → `BasePaginationUiState<ChapterUiModel>`; map with `toChapterUiModel()`; change `startedChapter: StateFlow<Chapter?>` to `startedChapterId: StateFlow<String?>`
- `MangaChapterItem.kt`: `Chapter` → `ChapterUiModel`
- `MangaChapterList.kt`: `List<Chapter>` → `List<ChapterUiModel>`
- `MangaChaptersSection.kt`: `BasePaginationUiState<Chapter>` → `BasePaginationUiState<ChapterUiModel>`
- `MangaDetailsContent.kt`: `startedChapter: Chapter?` → `startedChapterId: String?`
- `ActionButtonsSection.kt`: `startedChapter: Chapter?` → `startedChapterId: String?` + add `mangaId: String` param
- `MangaDetailsScreen.kt`: bind `startedChapterId` from VM

### Batch 6 — ReadingHistory
**Files:**
- `HistoryViewModel.kt`: `BasePaginationUiState<ReadingHistory>` → `BasePaginationUiState<ReadingHistoryUiModel>`; map with `toReadingHistoryUiModel()`
- `MangaDetailsViewModel.kt`: keep `_readingHistoryList: MutableStateFlow<List<ReadingHistory>>` private; expose `readingHistoryList: StateFlow<List<ReadingHistoryUiModel>>` via `.map { }.stateIn(...)`; change `continueChapter: StateFlow<ReadingHistory?>` → `StateFlow<ReadingHistoryUiModel?>`
- `ReadingHistoryItem.kt`, `ReadingHistoryInfo.kt`, `ReadingHistoryList.kt`, `HistoryContent.kt`: `ReadingHistory` → `ReadingHistoryUiModel`
- `MangaChapterList.kt`, `MangaChapterItem.kt`: `ReadingHistory?` → `ReadingHistoryUiModel?`
- `ActionButtonsSection.kt`: `continueChapter: ReadingHistory?` → `ReadingHistoryUiModel?`
- `MangaDetailsContent.kt`: update param types

### Batch 7 — ChapterPages
**Files:**
- `ReaderUiState.kt`: `ChapterPagesUiState.Success.chapterPages: ChapterPages` → `ChapterPagesUiModel`
- `ReaderViewModel.kt`: after cache/network success, call `.toChapterPagesUiModel()` before `ChapterPagesUiState.Success(chapterPages = ...)`; `chapterPages.totalPages` field name unchanged on UiModel
- `ReaderContent.kt`: change `chapterPageUiState.chapterPages.pages` → `chapterPageUiState.chapterPages.pageImageUrls`

---

## Architecture Decisions

1. **Naming convention**: `UiModel` suffix for all data/enum presentation models; `UiError` suffix for all error sealed classes.

2. **Translation boundary**: ViewModels map domain → UiModel. Composables only see UiModel types. No `domain.*` imports in composables.

3. **Private domain types in VMs**: When a VM needs domain type for use case calls AND UiModel for state, keep private domain field (e.g., `_domainManga: Manga?`, `currentDomainUser: User?`, `_readingHistoryList: MutableStateFlow<List<ReadingHistory>>`).

4. **`startedChapterId` pattern**: Expose only the chapter ID from VM (`startedChapterId: StateFlow<String?>`) instead of full `Chapter?`. Composables only need ID for navigation.

5. **Status formatting**: `status.replaceFirstChar` moved from composables into `MangaUiMapper`. `MangaUiModel.status` is always pre-formatted title-case.

6. **`ChapterPagesUiModel.pageImageUrls`**: URL construction (`$baseUrl/data/$dataHash/$page`) moved to `ChapterPagesUiMapper`. Composables receive ready-to-use URLs.

7. **`FavoriteManga.toManga()`**: Data-layer mapper function used in composable — CA violation. Removed in Batch 4. `FavoriteManga.toMangaUiModel()` is in presentation mapper instead.

8. **Domain exceptions**: Actual code uses `InfrastructureException`, `BusinessException`, `ValidationException` — NOT `RemoteException`/`MangaException` as in memory.md. Memory.md is outdated for exception types.
