# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

---

## Agent Rules

### Comments

**Never add comments** — no `//`, `/* */`, or KDoc (`/** */`) — unless explicitly requested by the
user. This applies globally across all layers (domain, data, presentation, util, baselineprofile).
Code should be self-explanatory through naming.

### Git

**All git commands must only be executed when explicitly requested or approved by the user.** Never
run autonomously: `git add/commit/push/checkout/switch/branch/reset/restore/clean`,
`gh pr create/merge`. When in doubt, show the command and ask.

### Skills

**Always invoke `android-cli`** for: emulator/AVD, run/deploy app, SDK management, screenshots, UI
layout inspection, docs search.
**Always invoke `android-adb`** for: list devices, install APKs, logcat, push/pull files.
**Always invoke `android-gradle`** for: Gradle build tasks, unit/instrumented tests, dependency
checks.

Do not ask for confirmation before invoking these skills — detect and invoke immediately.

---

## Project Setup

### Build Commands

```bash
./gradlew assembleDebug / assembleRelease / installDebug / clean
./gradlew testDebugUnitTest [--tests "com.example.FooTest.barTest"]
./gradlew connectedAndroidTest   # device required
```

### Required Config

`local.properties` must have `BASE_URL` + `UPLOAD_URL`. `app/google-services.json` required for
Firebase.

---

## Architecture

Three-layer Clean Architecture + MVVM. Dependency Rule: outer layers depend inward only.

```
domain/       Pure Kotlin. Entities, exceptions, repository interfaces, use cases.
data/         Implements domain interfaces. Retrofit, Room, Firebase, DataStore.
presentation/ Jetpack Compose UI, ViewModels, NavGraph.
di/           4 Hilt modules: LocalModule, RepositoryModule, ApiModule, FirebaseModule.
util/         CoroutineHandler, DateTimeHandler, NavTransitions.
```

---

## Domain Layer

Zero Android imports. `ValidationException` is thrown in `User` companion methods only — all other
exceptions are mapped at the data boundary.

**Business logic in companions** — call from VMs, never re-implement:

- `Chapter.determineNavPosition(currentId, list, hasNextPage): NavPosition` — sets prev/next chapter
  IDs
- `Chapter.isPrefetchNextPage(currentIndex, listSize): Boolean` — true within last 5 items
- `ReadingHistory.generateId(mangaId, chapterId): String` — `"${mangaId}_${chapterId}"`
- `ReadingHistory.findContinueTarget(list): ReadingHistory?` — first unfinished session
- `ReadingHistory.findInitialPage(chapterId, navChapterId, navPage, list): Int`
- `User.validateEmail/Password/ConfirmPassword/Name()` — throw `ValidationException` subtypes
- `ReadingStats.getLastDates(days = 7): List<String>` — last `days` dates (`yyyy-MM-dd`), oldest→newest
- `ReadingStats.buildWeeklyBreakdown(list): List<ReadingStats>` — zero-fills missing dates in the last
  7 days so chart data always has exactly 7 points
- `ReadingStats.buildMonthlyBreakdown(list): List<MonthlyReadingStat>` — groups the **entire** stats
  history by `yyyy-MM` (string-prefix grouping on `date`, no date parsing needed since the format is
  fixed), summed and sorted oldest→newest; returns a single zero-duration entry for the current month
  if `list` is empty, since `ColumnCartesianLayerModel` requires at least one series entry
- `ReadingStats.buildYearlyBreakdown(list): List<YearlyReadingStat>` — same shape/reasoning as
  `buildMonthlyBreakdown`, just grouped by `yyyy` (4-char date prefix) instead of `yyyy-MM`. All three
  breakdowns (`weekly`/`monthly`/`yearly`) are pure client-side aggregation over the same single
  `ObserveStatisticsUseCase` result — deliberately **not** separate persisted Firestore rollup
  documents. A persisted-rollup design (new collection, new `observe*` use case, new security rules)
  was drafted and even implemented for yearly before being reverted: it solves a read-cost problem
  (`observeStatistics` is a live, unbounded, unpaginated query over every daily doc a user has ever
  created) that doesn't actually exist yet at this app's usage scale, at the cost of a second Firestore
  collection + a batched atomic write + rules to keep in sync. Revisit that design specifically if/when
  the unbounded daily read becomes an actual measured cost — don't reach for it by default just because
  a new time-bucketed chart is being added.

**UNKNOWN rule**: never discard unrecognized API values — map to `UNKNOWN` enum entry.
**Default params**: repository interfaces define defaults; impls must not redefine them.

### Exception Hierarchy

```
DomainException
├── ValidationException — Email.{Empty|Invalid}, Password.{Empty|TooWeak},
│                         ConfirmPassword.{Empty|Mismatch}, Name.Empty
├── BusinessException  — Auth.{InvalidCredentials|UserNotFound|UserAlreadyExists|RegistrationFailed}
│                         Resource.{MangaNotFound|ChapterNotFound|ChapterDataNotFound|AccessDenied}
└── InfrastructureException — NetworkUnavailable (IOException), ServerUnavailable (HttpException), Unexpected
```

### Use Case Pattern

One-shot:

```kotlin
suspend operator fun invoke(id: String): Result<T> =
  CoroutineHandler.runSuspendResultCatching { repository.method(id) }
```

Reactive:

```kotlin
operator fun invoke(): Flow<Result<List<T>>> = repository.observe().toFlowResult()
```

---

## Data Layer

**Exception mapping** — choose by call site:

| Function                             | Use for                                             |
|--------------------------------------|-----------------------------------------------------|
| `toDomainException()`                | Retrofit catch blocks (HTTP + IO)                   |
| `toUnexpectedException()`            | Room / generic suspend catch; Auth Flow `.catch {}` |
| `toFirebaseFirestoreException()`     | Firestore **suspend** write/delete                  |
| `toFirebaseFirestoreFlowException()` | Firestore **Flow** `.catch {}`                      |
| `toFirebaseAuthException()`          | Firebase Auth suspend operations                    |

All functions rethrow `DomainException` and `CancellationException` unchanged.
**`CancellationException` guard in repos**: `is DomainException -> throw e` first, then remap
`HttpException`/`IOException`.

**Firebase Request DTOs**: `id` is `@get:Exclude` (becomes document ID); fields use
`@get:PropertyName("snake_case")`; timestamps use `@ServerTimestamp val field: Date? = null`.

**Firebase Response DTOs**: all fields are `var` (required for reflection); `id` is
`@get:Exclude @set:Exclude`, populated via `doc.toObject(...)?.copy(id = doc.id)`.

**Firestore paths**: `/users/{userId}` | `/users/{userId}/favorites/{mangaId}` |
`/users/{userId}/history/{historyId}` | `/statistics/{userId}_{date}` (flat top-level collection, not
nested under `/users/{userId}/...` like the other three — a deliberate exception, not an oversight:
`incrementReadingDuration` needs `FieldValue.increment()` on a partial write, which requires a raw
`Map<String, Any>` and a composite `"${userId}_${date}"` doc ID via `ReadingStats.generateId()`
rather than a request DTO addressed by `.document(id)`. Map keys still go through `FirestoreFields`
constants, not raw string literals, matching every other Firestore source in this codebase — only the
collection shape is the exception, not the string-literal discipline.)

**Cursor pagination**: all paginated Firestore queries use `startAfter(lastDocument).limit(n)` —
`null` lastItemId = first page.

**Enum wiring**: domain enums → `*Value` enums via `valueOf(name)` — names are identical across
layers. `ApiParamMapper` owns ISO codes/API strings; never put them in domain enums.

**Mappers**: all `object` singletons — `MangaMapper.toManga(dto)`, never instantiated. `UserMapper`
is the only bidirectional mapper (needed by `UpdateUserProfileUseCase`).

---

## DI Layer

4 modules, all `@InstallIn(SingletonComponent::class)`, all `@Singleton`.

| Module             | Type            | Provides                                                                 |
|--------------------|-----------------|--------------------------------------------------------------------------|
| `LocalModule`      | `object`        | Room `ChapterCacheDatabase`, `ChapterCacheDao`, `DataStore<Preferences>` |
| `RepositoryModule` | **`interface`** | 8 `@Binds` for all repository interface → impl bindings                  |
| `ApiModule`        | `object`        | Moshi, OkHttp (30s timeouts), Retrofit, `ApiService`                     |
| `FirebaseModule`   | `object`        | `FirebaseAuth`, `FirebaseFirestore`, 4 Firebase source `@Provides`       |

**Critical rules**:

- `RepositoryModule` is an **`interface`** (not `object`) — `@Binds` requires abstract functions
- Firebase sources use `@Provides` not `@Binds` — impls depend on `FirebaseAuth`/`FirebaseFirestore`
  provided in the same module
- **Moshi adapter order**: `IsoDateTimeAdapter` before `KotlinJsonAdapterFactory` — custom adapters
  must register first
- `NetworkInterceptor` is auto-resolved by Hilt — never add an explicit `@Provides` (causes
  duplicate binding error)
- `fallbackToDestructiveMigration(true)` safe only for `ChapterCacheDatabase` (re-fetchable cache);
  never for Firestore user data

---

## Util

**`CoroutineHandler`** — function selection:

- `runSuspendResultCatching { }` → `Result<T>`, catches `Throwable` — use in **use cases**
- `runSuspendCatching(context, block, catch)` → `T` directly, catches `Exception` only — use in *
  *repos** with exception remapping
- `Flow<T>.toFlowResult()` → `Flow<Result<T>>` — rethrows `CancellationException` — use in *
  *reactive use cases**

**`CancellationException` guard**: must appear **before** any `catch (e: Exception)` in VMs —
`toFlowResult()` rethrows it through `collect { }`.

**`DateTimeHandler`**: `String?.parseIso8601ToEpoch(): Long?` | `Long?.toTimeAgo(): String` — both
`SimpleDateFormat` use `ThreadLocal` (not thread-safe without it).

**`NavTransitions`**: `navigatePreserveState<Root>(route)` for tab/drawer switches (preserves state)
via `popUpTo<Root> { saveState = true }` + `restoreState = true` — `Root` MUST be the actual tab-root
destination that stays on the back stack forever (`NavRoute.Home`), never the `NavHost`'s graph-level
`startDestination` (`NavRoute.Splash`) — Splash is popped inclusively right after login, so a
`popUpTo` targeting it can never match again and save/restore silently no-ops, losing tab state on
every switch. `navigateClearStack<T>(route)` for auth flows — `T` is the route to pop inclusive (e.g.
`navigateClearStack<NavRoute.Login>(NavRoute.Home)`). `navigateTo`/`navigateBack` debounce internally
(500ms, one shared timer for the whole app) as a safety net against rapid double-navigation — kept
even though `Modifier.onClick` (see Compose Conventions) *also* debounces per click-instance, because
several navigation triggers (`AppTopBar`/`SearchBar` back and search icons, `MenuItemRow`'s drawer
items) go through raw Material3 `IconButton`/`NavigationDrawerItem` rather than `Modifier.onClick`,
and would otherwise have no protection at all.

---

## Presentation Layer

Domain types must never appear in composables or `UiState`. ViewModels are the only translation
boundary.

### UiState Patterns

| Pattern                    | When                       | Used by                                                   |
|----------------------------|----------------------------|-----------------------------------------------------------|
| Sealed interface           | Primary resource load      | `MangaSectionUiState`, `MangaDetailsUiState`, `CategoryListUiState`, `StatisticsUiState` |
| Data class                 | Form / fine-grained errors | `LoginUiState`, `RegisterUiState`, `ProfileUiState`       |
| `BasePaginationUiState<T>` | Infinite scroll            | CategoryDetails, Favorites, History, Search               |

All UiState/UiModel: `@Immutable`. Lists: `ImmutableList<T>` / `persistentListOf()`.

**Pull-to-refresh**: every list/data screen (Home, Categories, CategoryDetails, Search results,
Favorites, History, Statistics, MangaDetails) wraps its outermost `Box`/root in `*Content.kt` with
`PullToRefreshBox` (`@OptIn(ExperimentalMaterial3Api::class)`,
`rememberPullToRefreshState()`, `isRefreshing = false` **hard-coded** — the spinner auto-hides on
recomposition, it is never wired to a real loading boolean). `onRefresh: () -> Unit` is a required
(no-default) param threaded from the Screen: reuse the ViewModel's existing public first-page fetch if
one exists (e.g. `SearchViewModel.fetchMangaListFirstPage()`), otherwise add a public
`fun refresh() = xxxFirstPage()` wrapper around the private fetch (established by
`CategoriesViewModel.refresh()`) — never call `retry()`/`retryXxx()` for this, those are conditional
(only refetch on Error) and won't refresh already-successful data. `MangaDetailsViewModel.refresh()` is
the one deliberate exception to the "flash to Loading" behavior every other screen inherits for free
(their first-page fetchers already reset state to `Loading`/`FirstPageLoading` before fetching):
`fetchMangaDetails()`/`fetchFirstChapter()` don't reset `mangaDetailsUiState`, so refreshing a manga's
info/summary updates in place without wiping the page's cover-art background — correct for a heavy
detail screen with a background image, wrong for a plain list. Don't copy MangaDetails' pattern
elsewhere without the same reasoning.

**Charts**: use [Vico](https://github.com/patrykandpatrick/vico) (`com.patrykandpatrick.vico:compose-m3`,
version pinned in `gradle/libs.versions.toml`) — **not** a hand-rolled Canvas chart, and never a plain
text/number card once a chart can represent the same data (Statistics screen used to show 3 text
`StatCard`s; all 3 were removed and replaced by 2 charts — see below). This artifact requires
`compileSdk 37` (bumped from 36 specifically for this — the Android platform 37 SDK must be installed
locally); AGP 9.1.0 only officially tests up to compileSdk 36.1 and prints an advisory "unsupported
compile SDK" warning at build time, which is expected and non-fatal, not a real error.

Chart composables live in `presentation/screens/<screen>/components/`, and should be **generic over a
reusable chart-point presentation model**, not duplicated per time granularity — established by
`ReadingActivityChart(dataPoints: ImmutableList<ReadingChartPointModel>)` in `statistics/`, which
renders the weekly (7-day), monthly (`yyyy-MM`), **and** yearly (`yyyy`) charts off the same
composable, just fed different `dataPoints`. `ReadingChartPointModel(id, label, minutes)` is
intentionally generic (`label` is a weekday abbreviation for the weekly chart, a month/year label for
the other two) rather than three near-identical models — reuse this shape (or the same pattern) for any future
"one bar per bucket of time" chart rather than hand-rolling a new one-off model per screen.

Data flows in via `CartesianChartModelProducer` +
`LaunchedEffect(key) { modelProducer.runTransaction { columnModel { series(...) } } }` — `key` must be
the presentation-model list driving the chart (re-runs the transaction whenever data changes, mirrors
the `remember(key)` convention used elsewhere for derived state). Wrap the `CartesianChartHost` in
`ProvideVicoTheme(rememberM3VicoTheme())` so the chart's colors track `MaterialTheme.colorScheme`
(light/dark) automatically instead of hardcoding chart colors. Domain data for a chart must go through
the same domain→presentation-model mapping as everything else (`presentation/mapper/`) — never feed a
domain entity's raw fields straight into `columnModel`/`lineModel`. A `ColumnCartesianLayerModel`
series can never be empty (`ColumnCartesianLayerModel.kt` throws `require(entries.isNotEmpty())`), so
any aggregation feeding a chart must guarantee at least one data point even when the underlying list is
empty (`ReadingStats.buildMonthlyBreakdown` returns a single zero-duration entry for the current
period rather than an empty list — same reasoning `buildWeeklyBreakdown` already applied by always
returning exactly 7 zero-filled entries).

A chart doesn't have to fully replace every number — `StatisticsContent` keeps small `labelMedium`
caption lines (e.g. "Daily Reading Time: 12 min") above each chart for the aggregate figures a bar
chart doesn't make instantly readable (today's total, this week's total, all-time total), instead of
wrapping those numbers back in a boxed `Card` — the distinction that matters is "boxed stat card as the
primary content" (removed) vs. "plain caption text supporting a chart" (kept, and is good chart
accessibility practice per the `data-table`-alternative guidance — a chart should have a readable
numeric fallback nearby).

### Screen Structure

**Screen split**: `*Screen.kt` (VM injection, `collectAsStateWithLifecycle`) | `*Content.kt` (pure
composable, no VM) | `*ViewModel.kt` (business logic). `*Content` never calls `NavController`
directly.

**Hub screens (multiple feature VMs in one screen)**: `ProfileScreen` is the established example — it
shows a top-5 preview of Favorites / History / Statistics behind a `SectionHeader`'s "More »" that
navigates to the full dedicated screen. The hub has already loaded the data, so tapping "More »" must
**not** refetch — the dedicated screen renders the state that's already there. The mechanism:
`ProfileScreen`'s three feature VMs are created with plain `hiltViewModel()` inside
`composable<NavRoute.Profile>`, so they're scoped to **Profile's own `NavBackStackEntry`**; the
dedicated screens then retrieve *that same instance* with
`hiltViewModel(remember(it) { navController.getBackStackEntry<NavRoute.Profile>() })`. Those VMs live in
`common/viewmodels/{favorites,history,statistics}/` (with their UiState files), and the three *dedicated*
screens take a **required** `viewModel:` param with no `= hiltViewModel()` default — that's what forces
the caller to hand them Profile's instance instead of silently spinning up a second one.

**Two navigation invariants this depends on — breaking either one is a crash, not a glitch:**
`getBackStackEntry<T>()` throws `IllegalArgumentException` when `T` isn't on the back stack, so
(1) Favorites/History/Statistics are **deliberately not drawer items** (`MenuValue.isDrawerItem = false`,
`MenuDrawer` renders `MenuValue.drawerItems`) — Profile is their only entry point, guaranteeing it was
created first; and (2) Profile's "More »" uses plain `navigateTo`, **never** `navigatePreserveState`,
because the latter's `popUpTo<Home> { saveState = true }` would pop Profile off the stack on the way in.
If you ever re-add one of those three to the drawer, or switch "More »" back to `navigatePreserveState`,
this crashes immediately. Each screen still runs its own `SideEffect { …updateUserId(…) }` — harmless
now (the setters early-return when the value is unchanged) and it keeps each screen self-healing.

By contrast `MangaSectionViewModel` is scoped to Home's own entry via a plain `hiltViewModel()` default
on `HomeScreen` — it is **not** shared, and it cannot be scoped to `NavRoute.Splash` even though Splash
precedes Home, because `navigateClearStack<Splash>(Home)` pops Splash `inclusive = true`, destroying its
`ViewModelStore` exactly when Home needs it. Scope-to-a-parent-entry only works when that parent
provably stays on the back stack. A hub reuses each feature's VM **unchanged** — no new use case, no repo/domain change, no
`limit` param: the VMs already fetch a 20-item first page, so "top N" is just `.take(N)` in the section
composable. Per-section `*Section.kt` composables under `<screen>/components/sections/` own their own
state (e.g. History's two-option navigate dialog) and take flat callbacks. **Every** block of a hub is a
section in that folder, including the hub's own non-list content — Profile's avatar/name/email/Update
block is `ProfileEditSection`, sitting next to `ProfileFavoritesSection`/`ProfileHistorySection`/
`ProfileStatisticsSection`, so `*Content.kt` stays pure assembly (sections + screen-level dialogs) with
no layout details of its own. Spacing between sections comes from **one**
`Arrangement.spacedBy(16.dp)` on `*Content.kt`'s scrolling `Column` — sections themselves are passed a
bare `Modifier.fillMaxWidth()`, so the page rhythm is set in exactly one place instead of being
re-derived per section. `ProfileFavoritesSection`/`ProfileHistorySection`/`ProfileStatisticsSection`
declare `onMoreClick: (() -> Unit)? = null` (optional, last param) to match `SectionHeader`; the
trade-off is that forgetting to wire it is no longer a compile error, the "More »" link just silently
disappears. `ProfileEditSection` deliberately mirrors
`MenuHeader`'s horizontal shape (avatar left at its intrinsic 80dp — `ProfilePicture` is a fixed
`size(80.dp)`, so **don't** give it a `weight`, that just strands it in an oversized slot — then
name/email stacked in a `weight(1f)` column, `spacedBy(16.dp)`), with the Update button below the row.
`ProfileNameEdit` is `Arrangement.Start`-aligned for this reason: centered name next to a start-aligned
email inside the same column reads as broken. Name and email intentionally **wrap** rather than
ellipsize — they carry the user's own identity, so truncating them is worse than an extra line.
**Section-level errors render inline** (`LoadPageErrorMessage`, which has a retry button) inside a
fixed-height `Box` — never a modal `AlertDialog`, because several sections load in parallel and
concurrent modals would stack on top of each other. Fixed-height section boxes also stop the page from
jumping as each section resolves. **Section-level loading uses `ListLoadingIndicator`** (the slim
load-more bar from `common/indicators/`), **not `LoadingScreen`** — `LoadingScreen` is a whole-screen
treatment and reads as "the page is loading" when it's really just one strip of it; with several
sections resolving independently you'd get multiple full-screen spinners stacked down the page.
Screen-level dialogs and `LoadingScreen` (the hub's own update/logout) stay as normal.

**`ListLoadingIndicator` call sites**: the indicator is a single `LinearProgressIndicator` at
`fillMaxWidth(0.4f)`, self-centered inside its own `Box` — so **never give it horizontal padding**
(it can't reach the edges; padding only shrinks the bar). It's also only ~4dp tall, unlike the text
states it shares a `when` with (`LoadMoreMessage`/`AllItemLoadedMessage`/`LoadPageErrorMessage`, ~20dp),
so in a lazy list's load-more slot give it **vertical padding matching the `IDLE` branch at that same
site** — otherwise the row collapses and the list visibly jumps the moment you tap "Load More".

**Shared ViewModels** (`presentation/screens/common/viewmodels/`): a ViewModel whose instance must
outlive a single screen lives here instead of under its owning screen's package. `NavGraph()` (zero
params, called as `setContent { NavGraph() }` from `MainActivity` — there is no `DexReaderApp.kt`
composable anymore) is the single composition root that instantiates every shared ViewModel via
`hiltViewModel()` and threads it down as a param — a screen never calls `hiltViewModel()` for one of
these itself. `UserViewModel` (moved from top-level `presentation/`) exposes `isUserLoggedIn`/
`userProfile`, read by `NavGraph` and passed down as plain `isUserLoggedIn`/`currentUser` params to
every screen. `viewmodels/settings/SettingsViewModel` + `SettingsUiState` (moved from
`screens/settings/`, grouped under their own subpackage like `manga_section/` below) is read by
`NavGraph` to drive the app-wide `DexReaderTheme(themeOption = ...)` wrapping the whole `NavHost`, and
that same instance is passed into `ProfileScreen(settingsViewModel = ...)` — both consumers share one
instance instead of each calling its own `hiltViewModel()` (the original bug: `MainActivity` and the
old `SettingsScreen` each created an independent instance, so the two could desync). **There is no
Settings screen any more** — it was deleted and its only content (the theme picker) became
`ProfileSettingsSection` inside the Profile hub, so `NavRoute.Settings` and `MenuValue.SETTINGS` are
gone too; the drawer is down to Home / Categories / Profile. `SettingsViewModel` itself is untouched
and still lives in `common/viewmodels/settings/` because `NavGraph` needs `appliedThemeOption`
regardless of where the picker UI sits.
`viewmodels/manga_section/MangaSectionViewModel` + `MangaSectionUiState` (renamed from
`HomeViewModel`/`HomeUiState`, moved out of `screens/home/`) is instantiated once in `NavGraph` and
passed into `HomeScreen(viewModel = ...)` as a required param (no `= hiltViewModel()` default) — the
rename drops the Home-specific name so the same instance/type can be reused by other manga-listing
screens later.

**Full `uiState` vs. narrow flow at a wide-reach call site**: `NavGraph` collects
`settingsViewModel.uiState` directly (not a dedicated per-field flow) and reads `.appliedThemeOption`
off it for `DexReaderTheme` — kept simple on purpose, since `isLoading`/`isSuccess`/`isError` only
churn while the user is already on the Settings screen (which is recomposing for that anyway), so a
narrow slice would avoid recomposition that has no real-world payoff here. `UserViewModel` still
exposes `isUserLoggedIn`/`userProfile` as two separate flows instead of one bundled state — that split
earns its keep because those fields are read broadly across every screen, not just at `NavGraph`. Only
reach for a narrow slice when the wide-reach call site's own churn is otherwise wasted; don't add one
by default.

**Navigation**: `NavRoute` sealed interface with `@Serializable` members. `navigateClearStack()` for
auth flows; `navigatePreserveState()` for tab/drawer navigation. Value enums used as type-safe nav
args must be `@Serializable` (e.g. `MangaSortCriteriaValue`, carried on `NavRoute.CategoryDetails`).

**Generalized manga-list browse (one path, optional tag)**: a Home section (Trending / Latest Update /
New Release / Top Rated) is *just a preset sort criterion over the whole catalog with no tag filter* —
the four section endpoints and the tag endpoint all hit the same `GET /manga`, differing only by
`includedTags[]`, and Retrofit drops a null `@Query`. So the browse is unified end-to-end on a
**nullable** tag: `ApiService.getMangaList(tagId: String? = null, …)` →
`CategoryRepository.getMangaList(categoryId: String? = null, …)` → `GetMangaListUseCase`
(`domain/usecase/manga/`, not `category/` — it's no longer category-specific). `CategoryDetailsScreen`/
`CategoryDetailsViewModel` serve **both** entry points off `NavRoute.CategoryDetails(categoryTitle,
categoryId: String? = null, initialSortCriteria = LATEST_UPDATE)`: Categories/MangaDetails pass a
non-null `categoryId` (byte-identical to before); Home's per-section **"More »"** passes
`categoryId = null` + the section's mapped sort (`MangaSectionValue.toSortCriteriaValue()` in
`CriteriaMapper` — section→criteria is 1:1, with `NEW_RELEASE → MOST_VIEWED` since both are
createdAt-ordered). **The section browse's first page must match the Home row byte-for-byte**, so the
VM seeds criteria differently per entry point: `categoryId == null` (section) →
`CategoryDetailsCriteriaUiState.forSection(sortCriteria)`, which reproduces the section endpoint's exact
query — **no status + no content-rating filter** (empty `ImmutableList` ⇒ repo passes `emptyList()` ⇒
Retrofit omits the `@Query` ⇒ MangaDex server default), except `LATEST_UPDATE` which seeds
`status = [ON_GOING]` (the one section endpoint that filters status). `categoryId != null` (category)
keeps the plain `CategoryDetailsCriteriaUiState()` default (Ongoing + Safe). Do **not** give the section
browse the category's Ongoing+Safe default — that was the original bug: it narrowed/reordered the first
page so it no longer matched the Home row. Everything else (pagination/sort/filter machinery) is shared
unchanged. When adding another "browse all manga sorted by X" surface, reuse this route — do **not** add
a parallel use case/screen.

**Categories screen — genre cover-card grid**: `CategoriesContent` is a `LazyVerticalGrid`
(`GridCells.Fixed(2)`, full-span type headers via `item(span = { GridItemSpan(maxLineSpan) })` — the
`VerticalGridMangaList` pattern) grouped by `CategoryTypeValue` (Genre/Theme/Format/Content). Every
category is a `CategoryCard` built to match the **manga card** for app-wide consistency — same `Card`
(`shapes.medium`, elevation 2, `surfaceVariant`) + `MangaCoverArt` (so the same `R.drawable.placeholder`
shows on load/error) + `blurBackground` scrim, sized `fillMaxWidth × 250dp` like `MangaItem` — using its
**#1 TRENDING cover** as a cropped background + name overlay (the Netflix/Webtoon "genre tile"). Do not
re-hand-roll the cover/placeholder; reuse `MangaCoverArt`. The screen is for **browsing
many categories and picking one**, not consuming one — the single-category deep-dive is
`CategoryDetailsScreen`. (An earlier v1 stacked per-genre carousels/`HorizontalMangaList` here and was
reverted precisely because each carousel was a mini-CategoryDetails, i.e. it focused on one category at a
time; if tempted to add manga rows here again, don't — that's what CategoryDetails is for.) Each card
**lazily** loads its cover when it composes (`LaunchedEffect(category.id) { onLoadCover(id) }`); the VM
dedups (no-op if Loading/Success, re-fetch on Error). The cover fetch **reuses**
`GetMangaListUseCase(categoryId, limit = 1, sortCriteria = TRENDING, includeStats = false)` and takes
`.firstOrNull()?.coverUrl` in the VM — `includeStats = false` skips the `MangaStatsRepository` merge and
`limit = 1` fetches only the top-trending cover, so it's **one stats-less call, tiny payload** (no
separate cover use case — `GetMangaListUseCase` grew `limit`/`includeStats` params, defaulting to the
old behavior so the browse callers are byte-identical). Covers live in a **separate** `categoryCoverStates:
StateFlow<ImmutableMap<String, CategoryCoverUiState>>` (Loading/not-yet-loaded → `shimmerLoading` over the
`MangaCoverArt` placeholder; Success(coverUrl) → cropped cover; empty coverUrl or Error → the same
`R.drawable.placeholder` — a category is **never hidden**, always named + tappable). A card tap → `NavRoute.CategoryDetails(…, initialSortCriteria =
TRENDING)` so the detail's first item matches the card's cover. `CategoryRepository.getMangaList` gained a
`limit: Int = 20` param, and `GetMangaListUseCase` gained `limit`/`includeStats` params, for this.

### State Management

**Error dialog state**: `remember { mutableStateOf(false) }` +
`SideEffect(uiState) { if (uiState is Error) isShowErrorDialog = true }` — never key `remember` on
`uiState` (or a field of it) to derive the initial/show value; only the keyed effect may set it back to
`true`, so dismiss stays dismissed until the tracked condition flips again. For bundled (non-sealed)
UiState, key the effect on the specific boolean field (`uiState.isError`), not the whole state object,
so unrelated field changes (e.g. text input) don't re-arm the dialog. Uses keyed `SideEffect`
(`compose-runtime 1.12.0+`, via `composeBom 2026.08.00`), not `LaunchedEffect` — the body here is
always a synchronous state-flag flip or a plain (non-suspend) ViewModel setter call, never a
coroutine/suspend call, so the coroutine `LaunchedEffect` launches is pure overhead. This is also the
pattern for the `LaunchedEffect(isUserLoggedIn, currentUser?.id) { viewModel.updateUserId(...) }`
family at the top of most `*Screen.kt` files, and one-shot side-effect calls like `CategoryCard`'s
`LaunchedEffect(category.id) { onLoadCover(category.id) }` — same reasoning, same fix, same file.
**Keep `LaunchedEffect` when the body does any suspend work** — `delay()`, `Animatable.animateTo()`/
`.snapTo()`, `.collect()` on a flow, or calling a `suspend fun` — `SideEffect`'s `effect` is
`() -> Unit` with no `CoroutineScope`, so it cannot do any of that (`AnimatedLogoAndSlogan`,
`SplashScreen`, `MangaBanner`'s two pager-animation effects, `ReadingActivityChart`'s
`modelProducer.runTransaction { }` are all `LaunchedEffect` for exactly this reason, not oversights).
`SideEffect` also runs earlier than `LaunchedEffect`/`DisposableEffect` in the frame (during
composition's apply-changes phase, not dispatched to a coroutine) — fine for the state-flag/setter
pattern above, but worth re-checking case-by-case for anything timing-sensitive before converting.

**Staged vs. applied value for a wide-reach effect**: when a field is both (a) reflected immediately in
a screen's own UI and (b) drives a wider-reach effect that only takes hold once persisted, split it into
a staged field (updates the moment the user taps, for in-screen feedback) and an applied field (updates
only after the write succeeds) — never let one field serve both roles.
`SettingsUiState.selectedThemeOption` (tapped option, drives the radio highlight in
`ProfileSettingsSection`) vs. `appliedThemeOption` (persisted value, read by `NavGraph` to drive
`DexReaderTheme` — see Screen Structure) is the established example. Theme now **applies on tap**:
`ProfileScreen` calls `updateThemeOption(it)` then `saveThemeOption()` back to back (safe — the former
is a synchronous `MutableStateFlow.update`, so the latter reads the new staged value). There is
deliberately **no confirm dialog and no success dialog** any more: a theme switch inside a profile page
is a toggle, not a commitment, and the app repainting is its own confirmation — two modals to flip a
theme was the old Settings-screen behaviour and it did not survive the move. `resetThemeOption()` still
earns its keep: on a failed write it snaps `selectedThemeOption` back to `appliedThemeOption` so the
selection can't sit on a value that was never saved. The write error surfaces inline via
`LoadPageErrorMessage` with retry, matching the rest of the hub's sections. The picker itself is a
`Row` of three `ThemeOptionItem`s at `weight(1f)` each (icon + short label, centred) — the labels are
deliberately one word (`R.string.light`/`dark` are "Light"/"Dark", not "Light Mode"/"Dark Mode") so
three fit across a phone without truncating.

**Forcing a same-screen `HorizontalPager` jump from the ViewModel**:
`rememberPagerState(initialPage = ...)` only reads `initialPage` once — there's no built-in channel
for the ViewModel to move the pager afterward. `ChapterPagesSection` (Reader) reports the pager's
position outward via `LaunchedEffect(pagerState.currentPage) { onUpdateChapterPage(...) }` but has no
listener for external page changes. To force a jump within the same chapter (e.g.
`ReaderViewModel.resetChapterProgress()` snapping back to page 1), reuse the same unmount/remount
trick `navigateToPreviousChapter()`/`navigateToNextChapter()` already rely on: set
`_chapterPagesUiState.value = ChapterPagesUiState.Loading` (unmounts `ChapterPagesSection` —
`ReaderContent`'s `when` branch doesn't call it during `Loading`), then immediately set a new
`Success` with the target page — `rememberPagerState` re-reads `initialPage` fresh on remount.
`updateChapterPage()` alone cannot do this: it only `.copy()`s an existing `Success` and never assigns
`Loading`, so it never triggers the remount.

**Cancel in-flight mutation jobs before navigating away**: any `viewModelScope.launch` that captures a
`ChapterPagesUiState.Success` snapshot before a suspension point (e.g. a Firestore call) must be
tracked as a cancellable `Job` and cancelled at the top of any function that can change
`currentChapterId` mid-flight — otherwise the mutation's `.onSuccess` can fire later using the stale
snapshot, unmounting the *new* chapter's pager and resurrecting the *old* chapter's pages under the
new chapter's id. `ReaderViewModel.resetProgressJob` (cancelled, with `_resetProgressUiState` reset to
idle, at the top of both `navigateToPreviousChapter()`/`navigateToNextChapter()`) follows the same
established shape as `observeHistoryJob`/`cancelObserveHistoryJob()`.

### Compose Conventions

- No top-level `private val` in composable files — define variables directly inside the composable.
  Exception: theme tokens shared across multiple composables belong in `presentation/theme/`.
- `collectAsStateWithLifecycle()` not `collectAsState()`
- `OutlinedTextFieldDefaults.colors()` is `@Composable` — cannot be in `remember { }`. Use
  `val colorScheme = MaterialTheme.colorScheme`.
- All LazyList items: stable keys (`key = MangaModel::id`) — required whenever the list has a real
  unique ID. If items are plain values with no guaranteed uniqueness (e.g. raw search-suggestion
  strings), omit `key` entirely rather than key on the value or its `hashCode()` — duplicate keys
  crash the list at runtime (`IllegalArgumentException: Key "..." was already used`)
- `derivedStateOf { }` for scroll-driven booleans:
  `val show by remember { derivedStateOf { state.firstVisibleItemIndex > 0 } }`
- All `@Preview` wrapped in `DexReaderTheme { }`
- `Modifier.blur()` requires API 31+ (RenderEffect) — below that (app `minSdk = 24`) it silently
  no-ops, and it's a heavier hardware layer than the alternative even when it does run. Never use it
  for loading/dim overlays — use `Modifier.blurBackground(topAlpha, bottomAlpha)` (gradient, works on
  every API level, cheaper), the established pattern across every screen (Login, Register,
  ForgotPassword, History, Profile, Settings, MangaDetails)
- Coil `ImageRequest` passed to `AsyncImage` / `ZoomableAsyncImage` must be
  `remember(url) { ImageRequest.Builder(...).build() }` — never built inline in the call site.
  Established in `MangaCoverArt`, `ChapterPageImage`, `MangaDetailsBackground`, `ProfilePicture`
- Shared manga-info atoms live in `presentation/screens/common/badges/`: `MangaStatusBadge` (icon +
  label from `MangaStatusValue`), `MangaRatingChip` (star + rating), `MangaGenreChip` (label +
  optional `onClick`). All three share one visual family — `primaryContainer.copy(alpha = 0.9f)`
  pill, `shapes.small`, `tonalElevation = 4.dp` + `shadowElevation = 4.dp` — so badges/chips shown
  together never end up with mismatched elevation. Reuse these wherever manga status/rating/genre is
  shown instead of hand-rolling a new `Surface` or `Card` — established in `MangaItem`,
  `FavoriteMangaItem`, `MangaBanner`, `MangaInfoSection`, `MangaCategoryList` (Manga Details),
  `CategoryList` (Categories screen)
- `SectionHeader` (`presentation/screens/common/sections/`) — shared "icon + title + More »" row for any
  section of a bigger screen. Signature:
  `SectionHeader(icon: ImageVector, title: String, modifier, onMoreClick: (() -> Unit)? = null)`. It owns
  no padding — every call site passes `Modifier.fillMaxWidth().padding(start/end = 16.dp, top = 8.dp,
  bottom = 4.dp)`, matching how section spacing lives at the call site everywhere else. If you add a new
  caller, copy that padding; forgetting it is not a compile error, the header just sits flush against the
  screen edge while every other section is inset. **`onMoreClick` is nullable on purpose**: pass it when the section has a
  dedicated full screen behind it (Home's rows, Profile's Favorites/History/Statistics), leave it out
  when the section *is* the whole feature and there is nowhere to go — `ProfileSettingsSection` omits it,
  and the "More »" row simply isn't rendered. Never re-hand-roll this row — the "More »" affordance must
  stay visually identical everywhere it appears
- `AnimatedLogoAndSlogan` (`presentation/screens/common/animation/`) — shared hero logo used by both
  `SplashContent` and `AuthContent` (Login/Register/ForgotPassword). Takes `logoSize: Dp = 100.dp`
  (Splash passes `120.dp`; `AuthContent` uses the default, centered inside its own
  `Box(contentAlignment = Alignment.Center)` rather than passing a size override). Entrance is a
  **one-shot** fade+slide-in (`LaunchedEffect(Unit)`, runs once) — never make this loop/repeat: Auth
  screens keep this composable on-screen indefinitely while the user fills out a form, so a
  repeating fade in/out is a permanent distraction, and on Splash a repeating loop risks navigating
  away mid-fade-out, reading as a UI glitch. The icon circle itself is translucent —
  `primary.copy(alpha = 0.3f)` background + `shimmerHighlight` sweep, not a solid fill. This
  composable does **not** draw its own glow/halo — each host screen paints its own copy of
  `Brush.radialGradient(primary.copy(alpha = 0.3f) → Color.Transparent)` behind it instead:
  `SplashContent` applies it to its full-screen root `Box` (logo + loading bar both sit inside the
  glow), `AuthContent` scopes it to just its header `Box` (the `weight(0.3f)` region the logo lives
  in — the form region below keeps a plain `colorScheme.surface`, no gradient). Keep the gradient a
  per-screen background choice, not baked into `AnimatedLogoAndSlogan` itself, since each host may
  need a different glow extent.
- `ReadingProgressBar` (`presentation/screens/common/indicators/`) — shared page-count + percent +
  animated `LinearProgressIndicator` for reading progress, used by `MangaChapterItem` (manga details
  chapter list), `ReadingHistoryInfo` (history list), and `NavigateChapterBottomBar` (Reader's bottom
  bar center slot, between the prev/next chapter `IconButton`s, via `Modifier.weight(2f)`) so all
  three show identical progress info instead of some being percent-only and others page-count-only.
  M3 1.4's `LinearProgressIndicator` defaults to the "expressive" style (a gap near the end + a small
  stop-indicator dot) — pass `gapSize = 0.dp` and `drawStopIndicator = {}` to get the classic
  continuous bar needed for a compact list-row indicator
- `AppTopBar` (`presentation/screens/common/top_bars/`) — single composable replacing the former
  `MainTopBar`/`DetailsTopBar` pair; serves both `BaseScreen`'s Menu/drawer variant and
  `BaseDetailsScreen`'s/`ReaderScreen`'s Back variant. Three independent slots — `center`/`left`/
  `right` — each resolved via `when { xContent != null -> xContent(); xIcon != null -> ...; xTitle
  != null -> ... }`: `xContent: (@Composable () -> Unit)?` wins if set (`ReaderScreen`'s
  volume/chapter/title `centerContent`), else `xIcon: ImageVector?` renders as a `Box(Modifier.
  onClick(shape = CircleShape) { ... }) { Icon(...) }` (not a plain `IconButton`), else `xTitle:
  String?` renders as `Box(Modifier.onClick { onXClick() }) { Text(...) }` — `centerTitle` uses
  `titleLarge` (no weight override needed, see the typography-system bullet below), `leftTitle`/
  `rightTitle` use `bodyMedium + ExtraBold` but currently have no real caller (every screen only ever
  passes `leftIcon`/`rightIcon`/`centerContent`, never `leftTitle`/`rightTitle` — a dead-but-harmless
  path, not urgent to remove). All three slots default to `null`/no-op — a slot's presence is its
  own visibility switch, no separate `isEnabled`-style boolean (e.g. `rightIcon = if
  (isSearchEnabled) Icons.Default.Search else null`). Both `Icon(...)` calls hardcode
  `contentDescription = null`. Colors are 4 flat `Color` params (`containerColor`,
  `centerContentColor`, `leftContentColor`, `rightContentColor`) instead of a bundled
  `TopAppBarColors`, defaulting to the Back-variant look (`surfaceContainer`/`onPrimaryContainer`);
  `BaseScreen`'s Menu variant overrides all four plus wraps its own `AppTopBar(...)` call in a local
  `Surface(alpha = 0.95f, tonalElevation = 3.dp)` for the translucent tab-root look (`AppTopBar`
  itself has no alpha/elevation param — that wrapping is a call-site concern, not shared). 5 screens
  sit behind `BaseDetailsScreen` (`MangaDetailsScreen`, `CategoryDetailsScreen`, plus
  `FavoritesScreen`/`HistoryScreen`/`StatisticsScreen`) — **which wrapper a screen uses follows
  directly from whether it's a drawer tab**: `BaseScreen` (Menu icon + drawer) only for the four
  tabs still in `MenuValue.drawerItems` (Home, Categories, Profile, Settings); `BaseDetailsScreen`
  (Back icon) for everything reached *from* another screen. When Favorites/History/Statistics stopped
  being drawer tabs they moved to `BaseDetailsScreen` for exactly this reason — a hamburger that opens
  a drawer the screen isn't listed in is a dead end. Moving a screen across this line also drops its
  `onNavigateToMenuItemScreen`/`onNavigateToLoginScreen` params (drawer-only concerns) and adds
  `onNavigateBack`. `ForgotPasswordScreen`/`RegisterScreen` have no top bar at all (just `BackHandler`
  + a `*Content` call), despite what an earlier version of this doc claimed. `ReaderScreen` calls `AppTopBar`
  directly (not through `BaseDetailsScreen`, since it also needs a `bottomBar`/FAB/full-screen
  `AnimatedVisibility` toggle that `BaseDetailsScreen` doesn't expose): `centerContent` renders the
  volume/chapter number + chapter title (moved here from `NavigateChapterBottomBar`'s center slot —
  see `ReadingProgressBar` above for where the swap sends the progress bar) instead of a plain
  title string, `rightIcon` renders the reset-chapter-progress icon instead of the search icon.
- **Unified text-styling system** (audited and converged across all ~101 `Text()` call sites in
  `presentation/screens/` in one session): `presentation/theme/Type.kt` now customizes 11 Material3
  tokens with the custom `JsFont` family (was 9) — `titleLarge` carries `FontWeight.ExtraBold` baked
  directly into the token (was `Bold` + a `fontWeight = FontWeight.ExtraBold` override repeated at
  every call site — the override is gone everywhere now, `style = MaterialTheme.typography.
  titleLarge` alone is the complete "screen/section/dialog/bottom-sheet header" style). `
  headlineLarge` (32sp/40sp) and `headlineMedium` (28sp/36sp) are now also customized with `JsFont`
  (previously left at Material3's stock, uncustomized scale, which silently rendered in the wrong
  font family) — these two are reserved for hero/branding text that deliberately stays bigger than
  an ordinary `titleLarge` header (`AnimatedLogoAndSlogan`'s app name, `MangaBanner`'s hero manga
  title, the 3 auth screens' own titles, `StatisticsScreen`'s title) — don't use `headlineSmall`/
  `displayX` anywhere; they remain uncustomized and will silently break the font, exactly the bug
  this pass fixed. Established per-role conventions going forward: chip/badge labels
  (`MangaGenreChip`/`MangaStatusBadge`/`MangaRatingChip`/`MangaInfoSection`'s `InfoChip`) use
  `labelSmall + FontWeight.Black`; empty-state/pagination/not-found messages use `titleMedium +
  fontStyle = Italic + textAlign = Center` with no weight override (inherits the token's own
  SemiBold); chapter-row flavor text (the separator dot, chapter title in list rows, `ReaderScreen`'s
  top-bar subtitle) uses `labelMedium + fontStyle = Italic`; footer/quiet-caption text (menu drawer
  email, app-credit line, "Don't have an account?") uses its own base style + `fontStyle = Italic` +
  `color = onSurfaceVariant`, no weight override — this is distinct from a *clickable* inline link
  (`LoginForm`'s "Forgot Password?"/"Sign Up", `titleMedium + Bold + Italic + onPrimaryContainer`),
  which signals tappability via color and must not be folded into the quiet-caption convention. The
  Favorite/Unfavorite button's background is `FavoriteRed` (`presentation/theme/Color.kt`, alongside
  the existing `RatingStarGold`) — a deliberate brand color, not theme-adaptive by design, formalized
  from an inline hex literal rather than switched to `colorScheme.error`.

### Compose Performance

- **Don't wrap composable callbacks in `remember`.** Strong skipping (Kotlin 2.3.x, default-on since
  Compose compiler 2.0.20 — nothing in `app/build.gradle.kts`'s `composeCompiler {}` disables it)
  auto-memoizes lambdas **and** method references with unstable captures (like a `viewModel`) at a
  `@Composable` call site, so `remember { viewModel::method }` is redundant. Pass a plain
  `{ viewModel.method() }` lambda instead (audited: all ~51 sites across every `*Screen.kt` use this
  form, not `remember { viewModel::method }` — a lambda literal is *unambiguously* memoized, sidestepping
  any lingering method-reference doubt). **Scope matters — two things this does NOT cover, which still
  need `remember`:** (1) lambdas inside `LazyListScope.items { }` (non-`@Composable` — see the `items`
  bullet below); (2) `remember(key) { expensiveComputation() }` / `remember { mutableStateOf(...) }` /
  `remember { derivedStateOf { } }`, which are caching/state, not lambda memoization — always keep those.
- LazyList keys: never include index — stable server-side ID only
- Hoist `remember(list) { list.associateBy { it.id } }` before `items { }` — never `find { }` inside
- No backwards writes: never write to `MutableState` already read in the same composition pass
- `remember` keys must include ALL captured values that can change —
  `remember(item.id) { { cb(item.page) } }` silently stales if `page` changes but is not in the key
- `LazyListScope.items { }` / `LazyGridScope.items { }` are NOT `@Composable` — strong skipping does
  not auto-memoize lambdas inside; forward params directly or wrap with `remember(key) { }` per item
- `inline forEach` inside a `@Composable` content lambda runs in `@Composable` scope — strong
  skipping auto-memoizes; key per stable value: `remember(entry) { { cb(entry) } }`
- `derivedStateOf { }` only tracks `State<T>` reads — read `uiState.field` (through the State
  delegate), never a bare composable parameter; bare params are invisible to the tracker
- State scoped to a single page/item of `HorizontalPager` / `LazyColumn` / `LazyRow` (e.g.
  `isImageLoaded`) must be declared **inside** that content lambda, keyed per item
  (`remember(item.id) { mutableStateOf(...) }`) — never hoisted above the pager/list. Hoisting shares
  one instance across every page/item (`MangaBanner` bug: one shared flag turned off shimmer on every
  banner page as soon as a single image finished loading, and re-triggered recomposition of every
  composed page on each toggle)
- Custom animation modifiers that drive `graphicsLayer { }` or `drawWithContent { }` must read the
  animated `State<Float>` via `.value` **inside** that deferred block — never destructure via `by`
  at the top of the function. A `by` read there re-triggers full recomposition on every animation
  frame instead of a cheap redraw/relayout-only pass. `onClick`, `shimmerLoading`, `shimmerHighlight`,
  and `animateItemOnAppear` in `common/Modifiers.kt` all follow this correctly — use them as the
  reference pattern for any new animated modifier
- `Modifier.onClick(...)` is a plain `@Composable fun Modifier.onClick(...): Modifier` (not
  `composed { }`) — matches the other three modifiers above; avoid `composed { }` for new modifiers
  in this file, it adds a subcomposition per usage that a direct `@Composable` function doesn't need
- **Inside a plain `@Composable fun Modifier.foo(): Modifier`, `this` is the caller's entire
  incoming chain — never hand `this` to `.then(...)`.** Always build the appended segment from the
  `Modifier` companion: `.then(if (shape != null) Modifier.clip(shape) else Modifier)`, never
  `.then(if (shape != null) this.clip(shape) else this)`. The wrong form re-appends the whole caller
  chain, so every layout modifier the caller passed is applied *again* per `.then()`.
  **This is the exact migration hazard of dropping `composed { }`**: inside `composed { }` the
  factory receiver is the *empty* `Modifier` (materialization calls `factory(Modifier, …)`), so
  `this.clip(...)` was correct there; converting to a plain `@Composable` extension silently
  reinterprets every `this` without any compile error. `onClick` shipped this bug in `a80f140` and
  its two `.then(this…)` calls tripled the caller's chain — `MangaChapterItem`'s
  `padding(horizontal = 16.dp, vertical = 6.dp)` rendered as 48dp/18dp, `MangaItem`'s `padding(4.dp)`
  as 12dp — which is what the "UI bị padding thêm" reports across Home/Favorites/History/Search/
  CategoryDetails/MangaDetails actually were. `shimmerLoading`/`shimmerHighlight` always used the
  `Modifier` companion and were never affected. When a shared modifier is suspected, read its
  `.then()` receivers first — no padding literal changes, so grepping diffs for `padding`/`spacedBy`
  will not find it.
- **Never animate `scaleX`/`scaleY` in a list/grid item's entrance animation.** A `graphicsLayer`
  scale does not shrink the layout slot — only the drawn content — so a mid-animation item renders
  small inside a full-size slot and reads as extra padding around every item. `animateItemOnAppear()`
  animates only `alpha` + `translationY` for this reason. The entrance also replays whenever a
  LazyList item is re-composed (i.e. every time it scrolls back into view), so any such artifact
  shows during normal scrolling, not just on first load — `remember { }` inside the modifier cannot
  survive that. Transient scale is fine for press feedback (`onClick`), where the element is not one
  of many in a list.
- `MangaBanner`'s pager applies `scale`/`alpha` = `lerp(0.92f/0.6f, 1f, 1f - pageOffset)` per page
  in `graphicsLayer` — a banner screenshotted mid-auto-scroll is legitimately ~0.92× size and dimmer.
  Same caveat as above: judge banner spacing only from a settled pager, never mid-transition

---

## Maintenance

**Whenever architecture, build setup, or any layer's patterns change, this CLAUDE.md must be updated
in the same session.** This includes: new layers/modules, build command changes, exception hierarchy
changes, Hilt module changes, new utilities, use case/error-handling pattern changes, new
screens/composables/UiState/mappers/NavRoutes/Value enums.

**Large multi-file sessions** (e.g. a codebase-wide audit) log a dated entry in `CHANGELOG.md` at the
repo root — date, title, what changed. Append new entries there rather than creating new one-off
report files per session.
