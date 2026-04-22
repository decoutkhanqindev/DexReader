# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## IMPORTANT: Keep This File in Sync

**Whenever any file in the `presentation/` layer is added, removed, or modified, this CLAUDE.md must be updated in the same session to reflect the change.** This includes:

- New composable added → document its signature and usage pattern
- Composable signature changed → update the signature shown here
- New UiState field or UiModel class → update the UiModel Reference section
- New screen folder created → add it to the Package Structure
- New mapper function → update the Mapper Objects table
- New `NavRoute` entry → update the NavRoute list in the Navigation section
- New Value enum or new enum entry → update the Value Enums section
- New shared component in `screens/common/` → add to Common Components section
- Pattern changes (error handling, pagination, auth, etc.) → update the relevant section

Do not leave this file stale. An outdated CLAUDE.md is worse than no CLAUDE.md.

## Presentation Layer Rules

Domain types (`domain/entity/`, `domain/exception/`) must never appear in composables or `UiState` classes. **ViewModels are the only translation boundary** — they call use cases (domain types in), map results to `*UiModel`/`*Value` types (presentation types out), and expose only presentation types via `StateFlow`.

### Package Structure

```
presentation/
├── error/          FeatureError.kt, UserError.kt
├── mapper/         12 mapper objects (domain ↔ presentation conversion)
├── model/          UiModel data classes + Value enums
│   ├── category/   CategoryModel.kt
│   ├── manga/      MangaModel.kt, ChapterModel.kt, ChapterPagesModel.kt, FavoriteMangaModel.kt
│   ├── user/       UserModel.kt, ReadingHistoryModel.kt
│   └── value/      criteria/, category/, manga/, menu/, settings/
├── navigation/     NavRoute.kt (sealed interface), NavGraph.kt (NavHost)
├── theme/          Theme.kt, Color.kt, Type.kt, Shape.kt
└── screens/
    ├── common/     base/, menu/, lists/, states/, indicators/, buttons/, texts/, dialog/, image/
    ├── auth/       login/, register/, forgot_password/ + shared input fields
    ├── home/
    ├── search/
    ├── categories/
    ├── category_details/
    ├── manga_details/
    ├── reader/
    ├── favorites/
    ├── history/
    ├── profile/
    └── settings/
```

Each screen folder contains: `*Screen.kt`, `*ViewModel.kt`, `*UiState.kt`, `*Content.kt`, optionally `components/`.

---

## UiState Patterns

Two patterns are used. Choose by screen type:

**Sealed interface** — screens that load a primary resource (blank → loading → data/error):
```kotlin
@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val error: FeatureError = FeatureError.Generic) : HomeUiState
    data class Success(...) : HomeUiState
}
```
Used by: `HomeUiState`, `MangaDetailsUiState`, `CategoriesUiState`, `ChapterPagesUiState`

**Data class** — form screens or screens with fine-grained loading/error fields:
```kotlin
@Immutable
data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isSuccess: Boolean = false,
    val emailError: UserError? = null,
    val passwordError: UserError? = null,
    val isError: Boolean = false,
)
```
Used by: `LoginUiState`, `RegisterUiState`, `ForgotPasswordUiState`, `ProfileUiState`, `SettingsUiState`

**Pagination** — screens with infinite scroll (`screens/common/base/state/`):
```kotlin
@Immutable
sealed interface BasePaginationUiState<out T> {
    data object FirstPageLoading : BasePaginationUiState<Nothing>
    data class FirstPageError(val error: FeatureError = FeatureError.Generic) : BasePaginationUiState<Nothing>
    data class Content<T>(
        val currentList: ImmutableList<T> = persistentListOf(),
        val currentPage: Int = 1,
        val nextPageState: BaseNextPageState = BaseNextPageState.IDLE,
    ) : BasePaginationUiState<T>
}

enum class BaseNextPageState {
    IDLE, LOADING, NO_MORE_ITEMS, ERROR;

    companion object {
        // Returns IDLE if more pages exist, NO_MORE_ITEMS if last page
        fun fromPageSize(resultSize: Int, pageSize: Int): BaseNextPageState =
            if (resultSize >= pageSize) IDLE else NO_MORE_ITEMS
    }
}
```
Used by: `CategoryDetailsViewModel`, `FavoritesViewModel`, `HistoryViewModel`, `MangaDetailsViewModel` (chapter list), `SearchViewModel`

All `UiState` and `UiModel` classes are annotated `@Immutable`. List fields use `ImmutableList<T>` from `kotlinx.collections.immutable` (initialized with `persistentListOf()`). Map fields use `ImmutableMap`.

---

## ViewModel Pattern

```kotlin
@HiltViewModel
class XViewModel @Inject constructor(
    private val someUseCase: SomeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<XUiState>(XUiState.Loading)
    val uiState: StateFlow<XUiState> = _uiState.asStateFlow()

    fun doAction() {
        viewModelScope.launch {
            _uiState.update { XUiState.Loading }
            someUseCase(params)
                .onSuccess { result -> _uiState.update { XUiState.Success(result.toXModel()) } }
                .onFailure { e -> _uiState.update { XUiState.Error(e.toFeatureError()) } }
        }
    }
}
```

**`CancellationException` guard** — required in any VM that uses `try/catch` or has reactive flows with outer catch blocks. Always place before `catch (e: Exception)`:
```kotlin
catch (c: CancellationException) { throw c }
catch (e: Exception) { /* map exception */ }
```

**Private domain holders** — two VMs keep private domain-typed fields alongside their public `UiModel` state flows, because use cases require domain types as input:
- `MangaDetailsViewModel`: `private val _domainManga: MutableStateFlow<Manga?>` (needed by `AddToFavoritesUseCase`)
- `MangaDetailsViewModel`: `private val _readingHistoryList: MutableStateFlow<List<ReadingHistory>>` (needed for `findContinueTarget`); exposed as `StateFlow<List<ReadingHistoryModel>>` via `.map { }.stateIn(...)`

**Parallel async use cases** — used by `HomeViewModel` to load multiple lists simultaneously:
```kotlin
viewModelScope.launch {
    val def1 = async { useCase1() }
    val def2 = async { useCase2() }
    val def3 = async { useCase3() }
    val def4 = async { useCase4() }
    val results = awaitAll(def1, def2, def3, def4)
    if (results.all { it.isSuccess }) {
        _uiState.value = HomeUiState.Success(
            list1 = results[0].getOrThrow().map { it.toMangaModel() }.toPersistentList(),
            // ... repeat
        )
    } else {
        val e = results.firstOrNull { it.isFailure }?.exceptionOrNull()
        _uiState.value = HomeUiState.Error(e?.toFeatureError() ?: FeatureError.Generic)
    }
}
```

**Derived StateFlow** — for values computed from another StateFlow:
```kotlin
val availableLanguageList: StateFlow<ImmutableList<MangaLanguageValue>> =
    _mangaDetailsUiState
        .map { state ->
            if (state is MangaDetailsUiState.Success) state.manga.availableLanguages
            else persistentListOf()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), persistentListOf())
```

**userId flow control** — used by `FavoritesViewModel`, `HistoryViewModel`, `MangaDetailsViewModel`. Cancels and restarts inner observations when auth state changes:
```kotlin
private val _userId = MutableStateFlow<String?>(null)

fun updateUserId(userId: String?) { _userId.value = userId }

private fun observeData() {
    viewModelScope.launch {
        _userId.collectLatest { userId ->
            if (userId == null) {
                _uiState.value = BasePaginationUiState.FirstPageLoading
                return@collectLatest
            }
            observeDataUseCase(userId).collect { result -> /* handle */ }
        }
    }
}
```
The Screen calls `viewModel.updateUserId(currentUser?.id)` from `LaunchedEffect(isUserLoggedIn, currentUser?.id)`.

**Criteria UiState** — `CategoryDetailsViewModel` exposes two separate StateFlows:
- `categoryDetailsUiState: StateFlow<BasePaginationUiState<MangaModel>>` — the manga list
- `categoryCriteriaUiState: StateFlow<CategoryDetailsCriteriaUiState>` — filter/sort selections

**Domain→presentation map transform** — used by `CategoriesViewModel`:
```kotlin
val categoryMap = CategoryTypeValue.entries
    .filter { it != CategoryTypeValue.UNKNOWN }
    .associateWith { type ->
        (grouped[CategoryType.valueOf(type.name)] ?: emptyList())
            .map { it.toCategoryModel() }.toPersistentList()
    }
    .toImmutableMap()
```

---

## Screen / Content Separation

Every screen follows a three-file split:

| File | Responsibility |
|---|---|
| `*Screen.kt` | Hilt VM injection, `collectAsStateWithLifecycle()`, calls `*Content` |
| `*Content.kt` | Pure composable — receives UiState + callbacks, no VM dependency |
| `*ViewModel.kt` | Business logic, StateFlow, use case calls |

Screens pass navigation as lambda callbacks — `*Content` composables never call `NavController` directly.

**Base wrappers** (in `screens/common/base/`):

```kotlin
// For tab-level screens (Home, Categories, Favorites, History, Profile, Settings)
fun BaseScreen(
    isUserLoggedIn: Boolean,
    currentUser: UserModel?,
    selectedMenuItem: MenuValue,
    isSearchEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onNavigateToSignInScreen: () -> Unit,
    onNavigateToMenuItemScreen: (MenuValue) -> Unit,
    onNavigateToSearchScreen: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
)

// For detail screens (MangaDetails, CategoryDetails, Reader)
fun BaseDetailsScreen(
    title: String,
    isSearchEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToSearchScreen: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
)
```

**MenuDrawer** (`screens/common/menu/MenuDrawer.kt`):
```kotlin
fun MenuDrawer(
    isUserLoggedIn: Boolean,
    currentUser: UserModel?,
    drawerState: DrawerState,
    selectedItem: MenuValue,
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onItemClick: (MenuValue) -> Unit,
    content: @Composable () -> Unit,
)
```
Structure: `ModalNavigationDrawer` with `ModalDrawerSheet` containing `MenuHeader` (weight 0.4f) + `MenuBody` (weight 2f) + `MenuFooter` (weight 0.2f). `MenuHeader` shows avatar + name + email when logged in; shows a sign-in `SubmitButton` when logged out.

**User context propagation** — `NavGraph` receives `currentUser: UserModel?` and `isUserLoggedIn: Boolean` from `UserViewModel` and threads them to every screen. Screens propagate to VMs via:
```kotlin
LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(currentUser.id)
    else viewModel.updateUserId(null)
}
```

---

## Common Components

### List Components (`screens/common/lists/manga/`)

```kotlin
// Horizontal scrolling row — used on Home screen
fun HorizontalMangaList(
    items: ImmutableList<MangaModel>,
    modifier: Modifier = Modifier,
    onItemClick: (MangaModel) -> Unit,
)
// → LazyRow, Arrangement.spacedBy(2.dp), key = MangaModel::id
// → MangaItem sized 194×250.dp per item

// 2-column vertical grid — used on CategoryDetails, Search, Favorites screens
fun VerticalGridMangaList(
    items: ImmutableList<MangaModel>,
    modifier: Modifier = Modifier,
    onItemClick: (MangaModel) -> Unit,
    loadMoreContent: @Composable () -> Unit,  // rendered in full-span footer
)
// → LazyVerticalGrid(GridCells.Fixed(2)), key = MangaModel::id
// → Final item uses item(span = { GridItemSpan(maxLineSpan) }) for the load-more slot
// → MoveToTopButton overlaid at BottomEnd — animates scroll to top via coroutineScope

fun MangaItem(
    item: MangaModel,
    modifier: Modifier = Modifier,
    onClick: (MangaModel) -> Unit,
)
// → Card with onScalableClick(shape = CardDefaults.shape)
// → Box with shimmer(isEnable = !isImageLoaded) while Coil loads
// → MangaInfo overlay at bottom (85.dp height, 80% surface background)
```

**Image loading pattern** (`MangaCoverArt` + shimmer):
```kotlin
var isImageLoaded by rememberSaveable { mutableStateOf(false) }

Box(modifier = Modifier.shimmer(isEnable = !isImageLoaded)) {
    MangaCoverArt(
        url = item.coverUrl,
        title = item.title,
        modifier = Modifier.fillMaxSize()
    ) { isImageLoaded = true }   // onSuccess callback fires when Coil finishes
}
```
`MangaCoverArt` wraps Coil's `AsyncImage`. The `isImageLoaded` flag is `rememberSaveable` (survives recomposition). Once loaded, shimmer stops.

**CategoryItem** (`screens/common/lists/`): `onClick: (String, String) -> Unit` passes `(id, title)` — both are needed by callers for navigation.

### Status Screens (`screens/common/states/` and `screens/common/`)

```kotlin
fun LoadingScreen(modifier: Modifier = Modifier)
// → Full-screen app icon + LinearProgressIndicator; applies shimmer() to the whole column

fun IdleScreen(message: String, modifier: Modifier = Modifier)
// → Centered app icon + message text (FontWeight.Light) — used for empty states and login prompts

fun ListLoadingIndicator(modifier: Modifier = Modifier)
// → Row: LinearProgressIndicator | app icon | LinearProgressIndicator with shimmer

fun LoadMoreMessage(modifier: Modifier = Modifier, onClick: () -> Unit)
// → Row: HorizontalDivider | "Load more" text | HorizontalDivider — clickable to trigger next page
```

### Custom Modifiers (`screens/common/Modifier.kt`)

```kotlin
// Scale-down animation + ripple on click. shape clips the ripple.
fun Modifier.onScalableClick(shape: Shape? = null, block: () -> Unit): Modifier

// Shimmer effect from valentinilk/shimmer library. isEnable = false is a no-op.
fun Modifier.shimmer(isEnable: Boolean = true, durationMillis: Int = 1000, ...): Modifier

// Vertical gradient background — used on reader overlays
fun Modifier.blurBackground(color: Color, topAlpha: Float = 0.7f, bottomAlpha: Float = 1f): Modifier
```

**`onScalableClick` ripple radius conventions** (for `Card` composables):
| Radius | Card size |
|---|---|
| `120.dp` | Large vertical tile (MangaItem, FavoriteMangaItem) |
| `80.dp` | Full-width row (~100dp tall, e.g. ReadingHistoryItem) |
| `64.dp` | Expandable chapter row (~80dp tall, e.g. MangaChapterItem) |
| `40.dp` | Small chip/button (CategoryItem, SortCriteriaItem) |

---

## Auth Screen Patterns

**File structure** — 5 files per auth screen (login, register, forgot_password):
```
*Screen.kt      — Hilt VM injection, state collection, event collection
*ViewModel.kt   — form state + use case call
*UiState.kt     — @Immutable data class
components/
  *Content.kt   — dialog management + auth layout wrapper
  *Form.kt      — input fields + buttons
```

**Layout** (`AuthContent` — shared wrapper in `screens/auth/`):
```kotlin
// Column splits screen: 25% branding header / 75% form area
Column(modifier = modifier.background(colorScheme.surface)) {
    AuthHeader(modifier = Modifier.weight(0.25f).fillMaxWidth())
    Box(modifier = Modifier.weight(0.75f).fillMaxWidth()) { content() }
}
```

**Loading blur** — applied in `*Content.kt` on the outer `Box`:
```kotlin
Box(modifier = if (uiState.isLoading) modifier.blur(8.dp) else modifier) {
    AuthContent { LoginForm(...) }
    when {
        uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        uiState.isError -> if (isShowErrorDialog) NotificationDialog(...)
        uiState.isSuccess -> if (isShowSuccessDialog) NotificationDialog(...)
    }
}
```

**Dialog management** — each dialog flag is `rememberSaveable` in `*Content.kt`:
```kotlin
var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }
var isShowSuccessDialog by rememberSaveable { mutableStateOf(true) }
// Reset to true happens automatically on next isError/isSuccess transition because
// rememberSaveable keys off the composition scope, not the state value.
```

**Input fields** (shared in `screens/auth/`):
```kotlin
fun EmailInputField(
    value: String,
    error: UserError? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
)

fun PasswordInputField(
    value: String,
    isConfirmed: Boolean = false,   // true → label/icon show "Confirm Password"
    error: UserError? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
)

fun NameInputField(
    value: String,
    error: UserError? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
)
```
All three: `OutlinedTextField` + `isError = error != null` + `supportingText { error?.let { Text(stringResource(it.messageRes)) } }`. Password field toggles visibility via local `remember { mutableStateOf(false) }`.

**Error mapping in ViewModel** — exhaustive `when` on `toUserError()`:
```kotlin
.onFailure { throwable ->
    _uiState.update {
        when (val error = throwable.toUserError()) {
            is UserError.Email    -> it.copy(isLoading = false, emailError = error)
            is UserError.Password -> it.copy(isLoading = false, passwordError = error)
            is UserError.NotFound -> it.copy(isLoading = false, isError = true)
            else                  -> it.copy(isLoading = false, isError = true)
        }
    }
}
```
`updateEmail(v)` only clears `emailError`; `updatePassword(v)` only clears `passwordError`. They do NOT reset `isLoading`/`isSuccess`/`isError` (avoids dismissing a spinner mid-flight on keystrokes).

---

## Auth-Gated Screen Pattern

Used by **Favorites** and **History** screens.

**Conditional rendering** in `*Screen.kt`:
```kotlin
BaseScreen(...) {
    if (isUserLoggedIn) {
        FavoritesContent(uiState = uiState, ...)
    } else {
        IdleScreen(message = stringResource(R.string.please_sign_in_to_view_your_favorites))
    }
}
```

**userId flow control** in ViewModel — inner observation cancels and restarts on auth changes:
```kotlin
viewModelScope.launch {
    _userId.collectLatest { userId ->
        if (userId == null) {
            _uiState.value = BasePaginationUiState.FirstPageLoading
            return@collectLatest
        }
        observeUseCase(userId, limit = 20, lastItemId = null).collect { result ->
            result
                .onSuccess { /* update Content state */ }
                .onFailure { throwable ->
                    // Suppress AccessDenied while waiting for auth
                    if (throwable is BusinessException.Resource.AccessDenied && _userId.value == null) {
                        _uiState.value = BasePaginationUiState.FirstPageLoading
                        return@onFailure
                    }
                    _uiState.value = BasePaginationUiState.FirstPageError(throwable.toFeatureError())
                }
        }
    }
}
```

**Cursor-based pagination** (Favorites, History) — uses last item ID instead of page offset:
```kotlin
observeUseCase(userId = id, limit = PAGE_SIZE, lastItemId = list.lastOrNull()?.id)
```

**History two-part delete** — `RemoveFromHistoryUiState` holds the staged ID:
```kotlin
// Stage: user taps delete
viewModel.updateRemoveHistoryId(readingHistoryId)

// Confirm: user confirms dialog
viewModel.removeFromHistory()  // guards: isLoading || id == null → return
```

---

## Pagination Display Pattern

`BasePaginationUiState` is consumed in `*Content.kt` via a `when` dispatch:
```kotlin
when (val state = uiState) {
    is BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())
    is BasePaginationUiState.FirstPageError   -> IdleScreen(message = stringResource(state.error.messageRes))
    is BasePaginationUiState.Content          -> {
        VerticalGridMangaList(
            items = state.currentList,
            onItemClick = onItemClick,
            loadMoreContent = {
                when (state.nextPageState) {
                    BaseNextPageState.LOADING       -> ListLoadingIndicator(...)
                    BaseNextPageState.ERROR         -> LoadMoreMessage(...) { onRetryNextPage() }
                    BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(...)
                    BaseNextPageState.IDLE          -> { /* trigger fetch */ }
                }
            }
        )
    }
}
```

`VerticalGridMangaList` renders `loadMoreContent` in a full-width footer via:
```kotlin
item(span = { GridItemSpan(maxLineSpan) }) {
    Box(modifier = Modifier.fillMaxWidth()) { loadMoreContent() }
}
```

**Pagination guard** in ViewModel — prevents re-entry:
```kotlin
fun fetchNextPage() {
    val state = _uiState.value as? BasePaginationUiState.Content ?: return
    if (state.nextPageState == BaseNextPageState.LOADING ||
        state.nextPageState == BaseNextPageState.NO_MORE_ITEMS) return
    // proceed with fetch
}
```

---

## Reader Screen Patterns

The Reader is the most complex screen. Key patterns:

**Multi-phase load gate** — pages are only fetched after all metadata is ready:
```kotlin
private val _isFetchChapterDetailsDone = MutableStateFlow(false)
private val _isFetchMangaDetailsDone = MutableStateFlow(false)
private val _isObserveHistoryDone = MutableStateFlow(false)

combine(_isFetchChapterDetailsDone, _isFetchMangaDetailsDone, _isObserveHistoryDone) { cd, md, hd ->
    cd && md && hd
}.collectLatest { isAllDone ->
    if (isAllDone && _chapterPagesUiState.value is ChapterPagesUiState.Loading) fetchChapterPages()
}
```

**Cache-first chapter loading**:
```kotlin
getChapterCacheUseCase(chapterId).onSuccess { pages ->
    /* restore from cache — done */
    return@launch
}
// cache miss: fetch from network, then async-write to cache
getChapterPagesUseCase(chapterId, mangaId).onSuccess { pages ->
    addChapterCacheUseCase(pages)  // fire-and-forget cache write
}
```

**History restoration** — restores last read page on re-entry:
```kotlin
val initialPage = ReadingHistory.findInitialPage(
    chapterId = chapterIdToFetch,
    navChapterId = chapterIdFromNavArgs,
    navPage = lastReadPageFromNavArgs,
    historyList = currentReadingHistoryList
)
_chapterPagesUiState.value = ChapterPagesUiState.Success(
    currentChapterPage = initialPage,
    chapterPages = pages
)
```

**History write guard** — only persists user-driven page changes (not history restoration):
```kotlin
fun updateChapterPage(chapterPage: Int, isFromHistory: Boolean = false) {
    if (currentState.currentChapterPage == chapterPage) return
    _chapterPagesUiState.value = currentState.copy(currentChapterPage = chapterPage)
    if (!isFromHistory) addAndUpdateToHistory()
}
```

**Chapter navigation** — resets state machine for new chapter:
```kotlin
fun navigateToNextChapter() {
    val nextId = _chapterNavUiState.value.nextChapterId ?: return
    currentChapterId = nextId
    _chapterPagesUiState.value = ChapterPagesUiState.Loading
    _chapterDetailsUiState.value = ChapterDetailsUiState()   // clear metadata
    _isFetchChapterDetailsDone.value = false                  // re-trigger load gate
    fetchChapterDetails()
}
```

**AnimatedVisibility top/bottom bars** — toggled by double-tap (fullscreen mode):
```kotlin
var isFullScreen by rememberSaveable { mutableStateOf(false) }

Scaffold(
    topBar = {
        AnimatedVisibility(visible = !isFullScreen,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) { DetailsTopBar(...) }
    },
    // similar for bottomBar
    floatingActionButton = { ZoomPageButton { isFullScreen = !isFullScreen } }
)
```

**`HorizontalPager`** renders chapter pages; current page is tracked in `ChapterPagesUiState.Success.currentChapterPage` and written to history via `onUpdateChapterPage` callback.

---

## Error Types

**`FeatureError`** — used by all non-auth screens. Sealed class with `@StringRes messageRes`:
```kotlin
sealed class FeatureError(@param:StringRes val messageRes: Int) {
    data object NetworkUnavailable : FeatureError(R.string.no_internet_connection)
    data object ServerUnavailable  : FeatureError(R.string.error_server_unavailable)
    data object AccessDenied       : FeatureError(R.string.error_access_denied)
    data object MangaNotFound      : FeatureError(R.string.manga_not_available)
    data object ChapterNotFound    : FeatureError(R.string.chapter_not_available)
    data object Generic            : FeatureError(R.string.oops_something_went_wrong_please_try_again)
}
```

**`UserError`** — used only by auth screens. `@Stable` sealed class with nested sealed classes for field-level errors:
```kotlin
@Stable
sealed class UserError(@param:StringRes val messageRes: Int) {
    sealed class Email(@StringRes messageRes: Int) : UserError(messageRes) {
        data object Required    : Email(R.string.email_required)
        data object Invalid     : Email(R.string.invalid_email_format)
        data object AlreadyInUse: Email(R.string.error_email_already_in_use)
    }
    sealed class Password(@StringRes messageRes: Int) : UserError(messageRes) {
        data object Required    : Password(R.string.password_required)
        data object Weak        : Password(R.string.password_min_length)
        data object Incorrect   : Password(R.string.error_incorrect_password)
    }
    sealed class ConfirmPassword(@StringRes messageRes: Int) : UserError(messageRes) {
        data object Required    : ConfirmPassword(R.string.confirm_password_required)
        data object DoesNotMatch: ConfirmPassword(R.string.password_dont_match)
    }
    sealed class Name(@StringRes messageRes: Int) : UserError(messageRes) {
        data object Required    : Name(R.string.name_required)
    }
    data object NotFound           : UserError(R.string.error_user_not_found)
    data object NetworkUnavailable : UserError(R.string.no_internet_connection)
    data object RegistrationFailed : UserError(R.string.oops_something_went_wrong_please_try_again)
    data object Unexpected         : UserError(R.string.oops_something_went_wrong_please_try_again)
}
```

**`ErrorMapper`** (in `presentation/mapper/`):
- `Throwable.toFeatureError(): FeatureError` — for non-auth VMs
- `Throwable.toUserError(): UserError` — for auth VMs
Feature VMs import `ErrorMapper.toFeatureError` only; auth VMs import `ErrorMapper.toUserError` only.

---

## Mapper Objects

All in `presentation/mapper/`, all are `object` singletons:

| Mapper | Key functions |
|---|---|
| `MangaMapper` | `Manga.toMangaModel()`, `MangaStatus ↔ MangaStatusValue`, `MangaContentRating ↔ MangaContentRatingValue` |
| `ChapterMapper` | `Chapter.toChapterModel()` |
| `ChapterPagesMapper` | `ChapterPages.toChapterPagesModel()` — URL list copied as-is (constructed in data layer) |
| `CategoryMapper` | `Category.toCategoryModel()` |
| `LanguageMapper` | `MangaLanguage ↔ MangaLanguageValue` via `valueOf(name)` |
| `CriteriaMapper` | `MangaSortCriteriaValue.toMangaSortCriteria()`, `MangaSortOrderValue.toMangaSortOrder()` |
| `UserMapper` | `User.toUserModel()` + **`UserModel.toUser()`** (reverse — needed by `UpdateUserProfileUseCase`) |
| `ReadingHistoryMapper` | `ReadingHistory.toReadingHistoryModel()` |
| `FavoriteMangaMapper` | `FavoriteManga.toFavoriteMangaModel()` |
| `ThemeModeMapper` | `ThemeMode ↔ ThemeModeValue` |
| `MenuMapper` | `MenuValue.toNavRoute(): NavRoute` |
| `ErrorMapper` | `Throwable.toFeatureError()`, `Throwable.toUserError()` |

`LanguageMapper` and `CriteriaMapper` use `valueOf(name)` — domain enum names and `*Value` enum names are **identical** and must stay in sync.

---

## Value Enums

All in `presentation/model/value/`. Used in composables instead of domain enums.

- `MangaLanguageValue` — `@StringRes val value: Int` (70 entries, covers all MangaDex languages)
- `MangaStatusValue`, `MangaContentRatingValue`, `MangaSortCriteriaValue`, `MangaSortOrderValue`, `CategoryTypeValue` — `@StringRes val nameRes: Int`
- `ThemeModeValue` — `@StringRes val nameRes: Int` + `val icon: ImageVector`
- `MenuValue` — `@StringRes val nameRes: Int` + `val icon: ImageVector` (used in `MenuDrawer`)

**`rememberSaveable` for enum state:**
```kotlin
var sortCriteria by rememberSaveable(
    saver = Saver(save = { it.name }, restore = { MangaSortCriteriaValue.valueOf(it) })
) { mutableStateOf(MangaSortCriteriaValue.LATEST_UPDATE) }
```
For nullable enum state (e.g. `expandedType: CategoryTypeValue?`):
```kotlin
var expandedType by rememberSaveable(
    stateSaver = Saver(save = { it?.name }, restore = { it?.let { n -> CategoryTypeValue.valueOf(n) } })
) { mutableStateOf<CategoryTypeValue?>(null) }
```

---

## Navigation

`NavRoute` is a `sealed interface` with `@Serializable` members — enables type-safe Compose Navigation.

```kotlin
sealed interface NavRoute {
    @Serializable data object Login          : NavRoute
    @Serializable data object Register       : NavRoute
    @Serializable data object ForgotPassword : NavRoute
    @Serializable data object Home           : NavRoute
    @Serializable data object Categories     : NavRoute
    @Serializable data object Favorites      : NavRoute
    @Serializable data object History        : NavRoute
    @Serializable data object Profile        : NavRoute
    @Serializable data object Settings       : NavRoute
    @Serializable data object Search         : NavRoute
    @Serializable data class MangaDetails(val mangaId: String) : NavRoute
    @Serializable data class CategoryDetails(val categoryId: String, val categoryTitle: String) : NavRoute
    @Serializable data class Reader(val chapterId: String, val lastReadPage: Int = 0, val mangaId: String) : NavRoute
}
```

Routes with parameters use `data class`; routes without use `data object`.

Screens are wired in `NavGraph.kt`. Each `composable<NavRoute.X>` block calls the screen composable and supplies navigation lambdas. All transition animations are applied per route — do not add raw `enterTransition`/`exitTransition` on individual screens.

Navigation helpers in `util/NavTransitions.kt`:
- `navigateClearStack(route)` — used for post-auth navigation (clears auth screens from back stack)
- `navigatePreserveState(route)` — used for bottom-nav / drawer item switches

---

## Theme

`DexReaderTheme(themeOption: ThemeModeValue, content)` wraps the entire app. Called from `MainActivity` with the value from `SettingsViewModel`. Supports `LIGHT`, `DARK`, and `SYSTEM` (via `isSystemInDarkTheme()`). Dynamic color is supported on Android 12+.

All composables must be wrapped in `DexReaderTheme` for `@Preview` functions.

---

## Compose Conventions

- Collect StateFlow with `collectAsStateWithLifecycle()` (not `collectAsState()`) for lifecycle awareness.
- `OutlinedTextFieldDefaults.colors()` is `@Composable` and **cannot** be wrapped in `remember { }`. Use a local `val colorScheme = MaterialTheme.colorScheme` to consolidate repeated reads instead.
- `@Immutable` on UiState classes and `ImmutableList`/`ImmutableMap` for list/map fields are required — they enable Compose stability inference and prevent unnecessary recomposition.
- `derivedStateOf { }` for booleans derived from UiState in composables (e.g., `Profile` "show update button"):
  ```kotlin
  val isShowUpdateButton by remember(uiState) {
      derivedStateOf {
          val nameChanged = uiState.newName != null && uiState.newName != currentUser?.name
          val picChanged  = uiState.newAvatarUrl != null && uiState.newAvatarUrl != currentUser?.avatarUrl
          nameChanged || picChanged
      }
  }
  ```
- All LazyList items use stable keys: `key = MangaModel::id` (function reference) or `key = { it.id }` (lambda). This is required — without keys, Compose cannot reuse composition for moved items.

---

## UiModel Reference

All fields, for quick reference when scaffolding new screens or mappers:

```kotlin
@Immutable
data class MangaModel(
    val id: String,
    val title: String,
    val coverUrl: String,
    val description: String,
    val author: String,
    val artist: String,
    val categories: ImmutableList<CategoryModel>,
    val status: MangaStatusValue,
    val contentRating: MangaContentRatingValue,
    val year: String,
    val availableLanguages: ImmutableList<MangaLanguageValue>,
    val latestChapter: String,
    val updatedAt: String,       // pre-formatted via TimeAgo
)

@Immutable
data class ChapterModel(
    val id: String,
    val mangaId: String,
    val title: String,
    val number: String,
    val volume: String,
    val publishedAt: String,     // pre-formatted via TimeAgo
)

@Immutable
data class ChapterPagesModel(
    val chapterId: String,
    val pageImageUrls: ImmutableList<String>,
    val totalPages: Int,
)

@Immutable
data class FavoriteMangaModel(
    val id: String,
    val title: String,
    val coverUrl: String,
    val author: String,
    val status: MangaStatusValue,
)

@Immutable
data class CategoryModel(val id: String, val title: String)

@Immutable
data class UserModel(val id: String, val name: String, val email: String, val avatarUrl: String?)

@Immutable
data class ReadingHistoryModel(
    val id: String,
    val mangaId: String,
    val mangaTitle: String,
    val mangaCoverUrl: String,
    val chapterId: String,
    val chapterTitle: String,
    val chapterNumber: String,
    val chapterVolume: String,
    val lastReadPage: Int,
    val pageCount: Int,
    val lastReadAt: String,      // pre-formatted via TimeAgo
)
```

`UserMapper` is bidirectional — `User.toUserModel()` **and** `UserModel.toUser()` — because `ProfileViewModel` needs to pass a `User` to `UpdateUserProfileUseCase`. All other mappers are one-directional (domain → presentation).
