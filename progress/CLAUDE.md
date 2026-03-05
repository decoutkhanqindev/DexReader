# DexReader — Claude Session Handoff

## Status

**Current phase:** Phase 10 — Presentation Error Handling + ErrorMapper (complete)
**Overall progress:** All 10 phases complete. Full Tier 2 exception handling implemented; error
types centralized in `presentation/model/`; mapping centralized in
`presentation/mapper/ErrorMapper`.

---

## Completed This Session

### Phase 10A — Full Tier 2 Exception Handling (FeatureError wired into UiState)

- **`res/values/strings.xml`**: Added 3 new strings: `no_internet_connection`,
  `manga_not_available`, `chapter_not_available`
- **`presentation/screens/common/FeatureError.kt` [CREATED then moved]**: Sealed class with
  `Network`, `MangaNotFound`, `ChapterNotFound`, `Generic` subtypes
- **`BasePaginationUiState.kt`**: `data object FirstPageError` →
  `data class FirstPageError(val error: FeatureError = FeatureError.Generic)` (default keeps all
  existing call sites valid)
- **`HomeUiState.kt`, `SuggestionsUiState.kt`, `CategoriesUiState.kt`, `MangaDetailsUiState.kt`,
  `ReaderUiState.kt`**: Same `data object Error` →
  `data class Error(val error: FeatureError = FeatureError.Generic)` conversion
- **All `when` branch fixes**: Any `SomeState.FirstPageError ->` (equality) updated to
  `is SomeState.FirstPageError ->` (type check) in 5 VMs + `MangaChaptersSection.kt`
- **6 feature ViewModels**: Inline `when (throwable) { is DomainException.NetworkUnavailable →
  FeatureError.Network; ... }` added to each `onFailure` block
    - `MangaDetailsViewModel` also maps `MangaException.NotFound → FeatureError.MangaNotFound`
    - `ReaderViewModel` maps `MangaException.ChapterNotFound → FeatureError.ChapterNotFound` in
      `fetchChapterDetails`, `DomainException.NetworkUnavailable → FeatureError.Network` in
      `fetchChapterPages`
- **9 composables updated**: `HomeContent`, `SuggestionsSection`, `ResultsSection`,
  `CategoriesContent`, `CategoryDetailsContent`, `MangaDetailsContent`, `ReaderContent`,
  `FavoritesContent`, `HistoryContent` — added `is` checks + `stringResource(error.messageRes)`

### Phase 10B — ErrorMapper + Model Reorganisation

- **`presentation/model/UserError.kt` [NEW]**: `AuthError` renamed → `UserError`, moved from
  `presentation/screens/auth/AuthError.kt`; same nested structure but `UserError` name
- **`presentation/model/FeatureError.kt` [NEW]**: Moved from
  `presentation/screens/common/FeatureError.kt`;
  updated package name only
- **`presentation/mapper/ErrorMapper.kt` [NEW]**: `object` with two extension functions:
    - `fun Throwable.toFeatureError(): FeatureError` — maps all domain→feature error cases
    - `fun Throwable.toUserError(): UserError?` — maps all `UserException` subtypes; `null` for
      unrecognized exceptions
- **6 UiState files**: Import updated from `screens.common.FeatureError` → `model.FeatureError`
- **6 feature ViewModels**: Import updated + inline `when (throwable)` blocks replaced with
  `throwable.toFeatureError()`; `DomainException` and `MangaException` imports removed where
  no longer needed directly
- **3 auth ViewModels**: `UserException` + `AuthError` imports removed; replaced inline
  `when (throwable is UserException.*)` blocks with `when (val error = throwable.toUserError())`
  branching on `UserError` subtypes
- **3 UiState files + 3 composable input fields**: `AuthError` → `UserError` type references
- **`presentation/screens/auth/AuthError.kt` [DELETED]**: Class moved to `presentation/model/`
- **`presentation/screens/common/FeatureError.kt` [DELETED]**: Class moved to `presentation/model/`

---

## Next Session — Start Here

No deferred items remain. All 10 phases complete. Possible next steps:

- **Unit tests** for `ErrorMapper.toFeatureError()` and `ErrorMapper.toUserError()`
- **Unit tests** for exception mapping in repos and ViewModels (still no test coverage)
- Feature development as needed

---

## Important Context

- **`ErrorMapper` follows project `object` pattern**: `import ErrorMapper.toFeatureError` /
  `import ErrorMapper.toUserError` — same as `LanguageMapper`, `CriteriaMapper`, `ExceptionMapper`
- **`toUserError()` returns `UserError?`**: `null` for unrecognized/unmapped exceptions; auth VMs
  handle `null` via `else -> it.copy(isLoading = false, isError = true)`
- **`toFeatureError()` always returns non-null**: `else -> FeatureError.Generic` ensures every
  throwable maps to something displayable
- **`@param:StringRes` required on sealed class constructors**: Kotlin annotation target for
  constructor params — linter auto-corrects `@StringRes` → `@param:StringRes`
- **`ReaderViewModel` still imports `MangaException`**: The `fetchChapterDetails` conditional
  `if (throwable is MangaException.ChapterNotFound)` is control flow, not just mapping — kept
  intentionally
- **Default `FeatureError.Generic`**: Ensures all existing `Error()` / `FirstPageError()` no-arg
  call sites in VMs compile unchanged
- **`data object` → `data class` for error states**: Required to carry the `FeatureError` field;
  all equality-based `when` branches updated to `is`-checks

---

## Files Modified This Session

| File                                                                             | Change                                                   |
|----------------------------------------------------------------------------------|----------------------------------------------------------|
| `res/values/strings.xml`                                                         | +3 strings                                               |
| `presentation/model/FeatureError.kt`                                             | **[NEW]** moved from `screens/common/`                   |
| `presentation/model/UserError.kt`                                                | **[NEW]** renamed+moved from `screens/auth/AuthError.kt` |
| `presentation/mapper/ErrorMapper.kt`                                             | **[NEW]** centralized error mapping                      |
| `presentation/screens/auth/AuthError.kt`                                         | **[DELETED]** moved to model                             |
| `presentation/screens/common/FeatureError.kt`                                    | **[DELETED]** moved to model                             |
| `presentation/screens/common/base/BasePaginationUiState.kt`                      | `data object` → `data class` + FeatureError import       |
| `presentation/screens/home/HomeUiState.kt`                                       | same                                                     |
| `presentation/screens/search/SuggestionsUiState.kt`                              | same                                                     |
| `presentation/screens/categories/CategoriesUiState.kt`                           | same                                                     |
| `presentation/screens/manga_details/MangaDetailsUiState.kt`                      | same                                                     |
| `presentation/screens/reader/ReaderUiState.kt`                                   | same                                                     |
| `presentation/screens/auth/login/LoginUiState.kt`                                | `AuthError` → `UserError`                                |
| `presentation/screens/auth/register/RegisterUiState.kt`                          | same                                                     |
| `presentation/screens/auth/forgot_password/ForgotPasswordUiState.kt`             | same                                                     |
| `presentation/screens/auth/EmailInputField.kt`                                   | `AuthError` → `UserError`                                |
| `presentation/screens/auth/PasswordInputField.kt`                                | same                                                     |
| `presentation/screens/auth/NameInputField.kt`                                    | same                                                     |
| `presentation/screens/auth/login/LoginViewModel.kt`                              | toUserError() refactor                                   |
| `presentation/screens/auth/register/RegisterViewModel.kt`                        | toUserError() refactor                                   |
| `presentation/screens/auth/forgot_password/ForgotPasswordViewModel.kt`           | toUserError() refactor                                   |
| `presentation/screens/home/HomeViewModel.kt`                                     | toFeatureError() + import cleanup                        |
| `presentation/screens/categories/CategoriesViewModel.kt`                         | toFeatureError() + import cleanup                        |
| `presentation/screens/search/SearchViewModel.kt`                                 | toFeatureError() + import cleanup                        |
| `presentation/screens/category_details/CategoryDetailsViewModel.kt`              | toFeatureError() + import cleanup                        |
| `presentation/screens/manga_details/MangaDetailsViewModel.kt`                    | toFeatureError() + import cleanup                        |
| `presentation/screens/reader/ReaderViewModel.kt`                                 | toFeatureError() + import cleanup                        |
| `presentation/screens/home/components/HomeContent.kt`                            | `is` check + stringResource(error.messageRes)            |
| `presentation/screens/search/components/suggestions/SuggestionsSection.kt`       | same                                                     |
| `presentation/screens/search/components/results/ResultsSection.kt`               | same                                                     |
| `presentation/screens/categories/components/CategoriesContent.kt`                | same                                                     |
| `presentation/screens/category_details/components/CategoryDetailsContent.kt`     | same                                                     |
| `presentation/screens/manga_details/components/MangaDetailsContent.kt`           | same                                                     |
| `presentation/screens/reader/components/ReaderContent.kt`                        | same                                                     |
| `presentation/screens/favorites/components/FavoritesContent.kt`                  | same                                                     |
| `presentation/screens/history/components/HistoryContent.kt`                      | same                                                     |
| `presentation/screens/manga_details/components/chapters/MangaChaptersSection.kt` | `is` check fix                                           |
