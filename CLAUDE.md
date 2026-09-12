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

### Build Troubleshooting

**A compile that succeeds but ships a broken APK** — `ClassNotFoundException` at launch for a class
whose source is obviously fine (hit twice on `App`/`MainActivity`), a nonsense
`Unresolved reference`
to a symbol that exists, or a KSP `FileNotFoundException` on a generated `*_HiltModules.java` — is
corrupted incremental state in the AGP ASM transform (`transformDebugClassesWithAsm`, run by
Firebase
Perf/Crashlytics), which sits between `compileDebugKotlin` and `dexBuilderDebug`.
`compileDebugKotlin`
still reports BUILD SUCCESSFUL because its own output is intact; classes go missing downstream, so
the
dex and APK genuinely lack them. Confirm by listing
`app/build/intermediates/classes/debug/transformDebugClassesWithAsm/dirs/com/decoutkhanqindev/dexreader/`
— if `App.class`/`MainActivity.class` aren't there, that's it. **`./gradlew clean` is the fix**;
`--rerun-tasks` re-runs the task against the same output dir and does not help. Do **not** grep the
dex for the class name as a check — the name appears as a *reference* from Hilt-generated classes
even
when the class itself is absent, which reads as a false positive.

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
- `ReadingStats.getLastDates(days = 7): List<String>` — last `days` dates (`yyyy-MM-dd`),
  oldest→newest
- `ReadingStats.buildWeeklyBreakdown(list): List<ReadingStats>` — zero-fills missing dates in the
  last
  7 days so chart data always has exactly 7 points
- `ReadingStats.buildMonthlyBreakdown(list): List<MonthlyReadingStat>` — groups the **entire** stats
  history by `yyyy-MM` (string-prefix grouping on `date`, no date parsing needed since the format is
  fixed), summed and sorted oldest→newest; returns a single zero-duration entry for the current
  month
  if `list` is empty, since `ColumnCartesianLayerModel` requires at least one series entry
- `ReadingStats.buildYearlyBreakdown(list): List<YearlyReadingStat>` — same shape/reasoning as
  `buildMonthlyBreakdown`, just grouped by `yyyy` (4-char date prefix) instead of `yyyy-MM`. All
  three
  breakdowns (`weekly`/`monthly`/`yearly`) are pure client-side aggregation over the same single
  `ObserveStatisticsUseCase` result — deliberately **not** separate persisted Firestore rollup
  documents. A persisted-rollup design (new collection, new `observe*` use case, new security rules)
  was drafted and even implemented for yearly before being reverted: it solves a read-cost problem
  (`observeStatistics` is a live, unbounded, unpaginated query over every daily doc a user has ever
  created) that doesn't actually exist yet at this app's usage scale, at the cost of a second
  Firestore
  collection + a batched atomic write + rules to keep in sync. Revisit that design specifically
  if/when
  the unbounded daily read becomes an actual measured cost — don't reach for it by default just
  because
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
`/users/{userId}/history/{historyId}` | `/statistics/{userId}_{date}` (flat top-level collection,
not
nested under `/users/{userId}/...` like the other three — a deliberate exception, not an oversight:
`incrementReadingDuration` needs `FieldValue.increment()` on a partial write, which requires a raw
`Map<String, Any>` and a composite `"${userId}_${date}"` doc ID via `ReadingStats.generateId()`
rather than a request DTO addressed by `.document(id)`. Map keys still go through `FirestoreFields`
constants, not raw string literals, matching every other Firestore source in this codebase — only
the
collection shape is the exception, not the string-literal discipline.)

**Cursor pagination**: all paginated Firestore queries use `startAfter(lastDocument).limit(n)` —
`null` lastItemId = first page.

**Enum wiring**: domain enums → `*Value` enums via `valueOf(name)` — names are identical across
layers. `ApiParamMapper` owns ISO codes/API strings; never put them in domain enums.

**Mappers**: all `object` singletons — `MangaMapper.toManga(dto)`, never instantiated. `UserMapper`
is the only bidirectional mapper (needed by `UpdateUserProfileUseCase`).

**Localized text from MangaDex** — the API has **no server-side locale negotiation**. Verified
against the live API: `Accept-Language`, `X-Locale`, `?locale=`, `?lang=`, `?language=` are all
ignored (byte-identical responses), and `availableTranslatedLanguage[]` only *filters which manga*
come back, it does not narrow the text fields. So the client always receives the whole map and picks
a key itself. Which fields are maps: `manga.title`, `manga.altTitles` (a **list** of maps),
`manga.description`, `tag.name`, `tag.description`. `chapter.title` is a plain `String` — a chapter
is a single translation, its language is `translatedLanguage`.

`LocalizedTextMapper.localized(languageCode, altTexts)` is the one place that resolves them, chained
`preferred → preferred-in-altTexts → en → en-in-altTexts → first value`. Both `MangaMapper.toManga`
and `CategoryMapper.toCategory` take `preferredLanguage: MangaLanguage = MangaLanguage.ENGLISH` and
feed it through — **never re-hardcode `"en"` in a mapper**, which is what they both used to do.

`altTexts` matters more than it looks: measured over the 1000 most-followed manga, `title` is keyed
`ja-ro` **81%** of the time and `en` only **15%**, so `title["en"]` misses almost always and the old
fallback showed romanized Japanese. Reading `altTitles` fixes **86%** of displayed titles
("Na Honjaman Level-Up" → "Solo Leveling", "Sono Bisque Doll wa Koi o Suru" → "My Dress-Up
Darling").
Coverage for a Vietnamese preference: `altTitles` has `vi` on 62% of manga, `description` on 19%,
and 59% have Vietnamese chapters. **Tag names are `en`-only** in MangaDex data — localizing genre
names has to be done with in-app string resources, there is nothing to read from the API.

`availableTranslatedLanguages` is **not trustworthy for fallback decisions** — it is stale.
Measured:
5 of 12 sampled manga declare a language whose chapter feed then returns `total = 0` (e.g.
"Na Honjaman Level-Up" declares `vi`, feed returns nothing under every `contentRating` /
`includeExternalUrl` / `includeFuturePublishAt` combination). To fall back, request the preferred
language and check `total == 0`, then re-request English. Do **not** send two languages in one call:
the feed mixes them with no priority (10 `en` + 7 `vi` in one page) and pagination interleaves them.

**Where the preferred language comes from**: `SettingsRepository.observeContentLanguage()` (same
DataStore as the theme pair, so no DI change), read *inside* the data layer —
`MangaRepositoryImpl`, `CategoryRepositoryImpl` and `ChapterRepositoryImpl` inject
`SettingsRepository` and resolve it themselves. That is deliberate: threading a `preferredLanguage`
param up through every use case and ViewModel would have touched ~8 repository methods and every
caller, for a value none of them actually decide. Read it **once per repository method**, never
inside a `mapNotNull` lambda — `MangaRepositoryImpl.toMangaList()` exists exactly so the `.first()`
happens once per response rather than once per manga.

**Chapter language fallback** lives in `ChapterRepository.resolveChapterLanguage(mangaId)`, not in
`getChapterList`: the repository cannot tell whether a caller's `language` was an explicit user pick
from `ChapterLanguageListBottomSheet` or just a default, and silently overriding an explicit pick
would be wrong. `MangaDetailsViewModel.resolveChapterLanguageThenFetch()` calls it once in `init`
**before** the first `fetchFirstChapter()`/`fetchChapterListFirstPage()`, then seeds
`_chapterLanguage`; `updateChapterLanguage()` (the explicit pick) never goes through it. The impl
short-circuits when the preferred language is already English — the fallback target — so the common
case costs no extra request; otherwise it probes with `limit = 1` and falls back on an empty result.

`availableLanguages` is `.distinct()`-ed in `MangaMapper` on purpose: there is **no `UNKNOWN`
language entry** — `ApiParamMapper.toMangaLanguage()` maps every code the enum doesn't recognize to
`MangaLanguage.ENGLISH`, so a manga translated into two unmapped languages would otherwise yield
duplicate `ENGLISH` entries and crash `ChapterLanguageListBottomSheet`, whose
`items(key = LanguageValue::name)` rejects duplicate keys. (This replaced the old `UNKNOWN`
enum entry + `distinct` on duplicate `UNKNOWN`s; the dedup is still required, only the collapsed
value changed.)

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

**`NavTransitions`**: `navigatePreserveState<Root>(route)` is the **bottom-tab switch**
(preserves each tab's state) via `popUpTo<Root> { saveState = true }` + `launchSingleTop` +
`restoreState = true` — `Root` MUST be the destination that stays on the back stack forever, i.e.
the **inner** tab
`NavHost`'s `startDestination` (`NavRoute.Home`), never a graph-level `startDestination` that gets
popped (`NavRoute.Splash` is popped inclusively right after Splash, so a `popUpTo` targeting it can
never match again and save/restore silently no-ops, losing tab state on every switch). Dropping the
`popUpTo` would give
back-to-previous-tab, but then `saveState`/`restoreState` have nothing to hook into and every tab
switch rebuilds the tab from scratch (new `NavBackStackEntry` ⇒ new `ViewModelStore` ⇒ refetch +
lost
scroll) — state preservation is the reason this shape was chosen. The back question is moot anyway:
`MainScreen` swallows Back entirely (see Bottom navigation below), so tab switching is
tap-only. `navigateClearStack<T>(route)` for
auth flows — `T` is the route to pop inclusive (e.g.
`navigateClearStack<NavRoute.Login>(NavRoute.Main)`).

**The two pop helpers are `inline` + `reified`, and the pop target must stay a type argument.** A
`popUpTo<T> { }` compiles to `NavOptionsBuilder.popUpTo(route: KClass<T>, …)` (verified in the
bytecode: `popUpTo:(Lkotlin/reflect/KClass;…)`), which matches a destination **by route pattern**.
A value-typed pop target instead binds `popUpTo(route: T, …)`, and that overload behaves differently
in two ways worth knowing before anyone "simplifies" the generics away: it matches by the
**fully-filled route including arguments** (so a data-class route like `NavRoute.MangaDetails` would
need the exact instance off the back stack via `entry.toRoute<…>()`, not a freshly built one), and
its
`generateRouteFilled()` **throws `IllegalArgumentException`** when the route isn't declared in that
controller's own graph, where the reified/`KClass` path merely logs `Ignoring popBackStack …` and
returns false. With two `NavHost`s (tab routes only in the inner graph, everything else only in the
outer) that second difference is a live crash risk, which is why this stayed reified.

The routes themselves are still typed: `route: NavRoute` and `Root`/`T : NavRoute`, not `Any` —
`NavController.navigate` only asks for `Any`, so the narrowing is ours, and it makes passing a
non-route a compile error. Cost: `util/NavTransitions.kt` imports
`presentation.navigation.NavRoute`,
so this one util file is knowingly coupled to the presentation layer (it is navigation glue, not a
general-purpose helper like `CoroutineHandler`/`DateTimeHandler`). `navigateTo`/`navigateBack` are
thin
wrappers over `navigate`/`popBackStack` with **no** debounce of their own; rapid double-navigation
is
guarded per click-instance by `Modifier.onClick` (see Compose Conventions), which `AppBottomBar` and
`AppTopBar`'s icon slots go through — `SearchBar`'s back arrow is still a raw Material3 `IconButton`
and has no such guard.

---

## Presentation Layer

Domain types must never appear in composables or `UiState`. ViewModels are the only translation
boundary.

### UiState Patterns

| Pattern                    | When                       | Used by                                                                                  |
|----------------------------|----------------------------|------------------------------------------------------------------------------------------|
| Sealed interface           | Primary resource load      | `MangaSectionUiState`, `MangaDetailsUiState`, `CategoryListUiState`, `StatisticsUiState` |
| Data class                 | Form / fine-grained errors | `LoginUiState`, `RegisterUiState`, `ProfileUiState`                                      |
| `BasePaginationUiState<T>` | Infinite scroll            | CategoryDetails, Favorites, History, Search                                              |

All UiState/UiModel: `@Immutable`. Lists: `ImmutableList<T>` / `persistentListOf()`.

**Pull-to-refresh**: every list/data screen (Home, Categories, CategoryDetails, Search results,
Favorites, History, Statistics, MangaDetails) wraps its outermost `Box`/root in `*Content.kt` with
`PullToRefreshBox` (`@OptIn(ExperimentalMaterial3Api::class)`,
`rememberPullToRefreshState()`, `isRefreshing = false` **hard-coded** — the spinner auto-hides on
recomposition, it is never wired to a real loading boolean). `onRefresh: () -> Unit` is a required
(no-default) param threaded from the Screen: reuse the ViewModel's existing public first-page fetch
if
one exists (e.g. `SearchViewModel.fetchMangaListFirstPage()`), otherwise add a public
`fun refresh() = xxxFirstPage()` wrapper around the private fetch (established by
`CategoriesViewModel.refresh()`) — never call `retry()`/`retryXxx()` for this, those are conditional
(only refetch on Error) and won't refresh already-successful data. `MangaDetailsViewModel.refresh()`
is
the one deliberate exception to the "flash to Loading" behavior every other screen inherits for free
(their first-page fetchers already reset state to `Loading`/`FirstPageLoading` before fetching):
`fetchMangaDetails()`/`fetchFirstChapter()` don't reset `mangaDetailsUiState`, so refreshing a
manga's
info/summary updates in place without wiping the page's cover-art background — correct for a heavy
detail screen with a background image, wrong for a plain list. Don't copy MangaDetails' pattern
elsewhere without the same reasoning.

**Charts**: use [Vico](https://github.com/patrykandpatrick/vico) (
`com.patrykandpatrick.vico:compose-m3`,
version pinned in `gradle/libs.versions.toml`) — **not** a hand-rolled Canvas chart, and never a
plain
text/number card once a chart can represent the same data (Statistics screen used to show 3 text
`StatCard`s; all 3 were removed and replaced by 2 charts — see below). This artifact requires
`compileSdk 37` (bumped from 36 specifically for this — the Android platform 37 SDK must be
installed
locally); AGP 9.1.0 only officially tests up to compileSdk 36.1 and prints an advisory "unsupported
compile SDK" warning at build time, which is expected and non-fatal, not a real error.

Chart composables live in `presentation/screens/<screen>/components/`, and should be **generic over
a
reusable chart-point presentation model**, not duplicated per time granularity — established by
`ReadingActivityChart(dataPoints: ImmutableList<ReadingChartPointModel>)` in `statistics/`, which
renders the weekly (7-day), monthly (`yyyy-MM`), **and** yearly (`yyyy`) charts off the same
composable, just fed different `dataPoints`. `ReadingChartPointModel(id, label, minutes)` is
intentionally generic (`label` is a weekday abbreviation for the weekly chart, a month/year label
for
the other two) rather than three near-identical models — reuse this shape (or the same pattern) for
any future
"one bar per bucket of time" chart rather than hand-rolling a new one-off model per screen.

Data flows in via `CartesianChartModelProducer` +
`LaunchedEffect(key) { modelProducer.runTransaction { columnModel { series(...) } } }` — `key` must
be
the presentation-model list driving the chart (re-runs the transaction whenever data changes,
mirrors
the `remember(key)` convention used elsewhere for derived state). Wrap the `CartesianChartHost` in
`ProvideVicoTheme(rememberM3VicoTheme())` so the chart's colors track `MaterialTheme.colorScheme`
(light/dark) automatically instead of hardcoding chart colors. Domain data for a chart must go
through
the same domain→presentation-model mapping as everything else (`presentation/mapper/`) — never feed
a
domain entity's raw fields straight into `columnModel`/`lineModel`. A `ColumnCartesianLayerModel`
series can never be empty (`ColumnCartesianLayerModel.kt` throws `require(entries.isNotEmpty())`),
so
any aggregation feeding a chart must guarantee at least one data point even when the underlying list
is
empty (`ReadingStats.buildMonthlyBreakdown` returns a single zero-duration entry for the current
period rather than an empty list — same reasoning `buildWeeklyBreakdown` already applied by always
returning exactly 7 zero-filled entries).

A chart doesn't have to fully replace every number — `StatisticsContent` keeps small `labelMedium`
caption lines (e.g. "Daily Reading Time: 12 min") above each chart for the aggregate figures a bar
chart doesn't make instantly readable (today's total, this week's total, all-time total), instead of
wrapping those numbers back in a boxed `Card` — the distinction that matters is "boxed stat card as
the
primary content" (removed) vs. "plain caption text supporting a chart" (kept, and is good chart
accessibility practice per the `data-table`-alternative guidance — a chart should have a readable
numeric fallback nearby).

### Screen Structure

**Screen split**: `*Screen.kt` (VM injection, `collectAsStateWithLifecycle`, **navigation**) |
`*Content.kt` (pure composable, no VM) | `*ViewModel.kt` (business logic). `*Content` never calls
`NavController` directly — it takes flat `on*Click` callbacks and the `*Screen.kt` above it turns
those into `navController.navigateTo(...)`.

**Every `*Screen.kt` takes `navController: NavHostController` as its first param and navigates
itself** — there are no `onNavigateToXxxScreen: () -> Unit` params threaded down from `NavGraph`
any more. `NavGraph`'s destinations are therefore near-uniform three-liners
(`navController = navController` + the screen's VMs/flags + `modifier`), and adding a navigation
edge is a one-line change inside the screen that owns the click, not a new param on every composable
between it and `NavGraph`. Screens on the **inner** tab host (Home/Categories/Profile) receive the
**outer** controller for the same reason they always did: everything they navigate to
(Search, MangaDetails, CategoryDetails, Settings, Favorites/History/Statistics, Login) lives on the
outer host, and `ProfileScreen`'s Sign In does `navigateClearStack<NavRoute.Main>(NavRoute.Login)`,
which can only target the outer back stack. The two `BaseScreen`/`BaseDetailsScreen` wrappers keep
their `onNavigateBack`/`onNavigateToSearchScreen`/`onNavigateToSettingsScreen` lambda params — they
are shared UI shells with no route knowledge, and every caller now passes
`{ navController.navigateBack() }` / `{ navController.navigateTo(NavRoute.Search) }` inline.

**Hub screens (multiple feature VMs in one screen)**: `ProfileScreen` is the established example —
it
shows a top-5 preview of Favorites / History / Statistics behind a `SectionHeader`'s "More »" that
navigates to the full dedicated screen. The hub has already loaded the data, so tapping "More »"
must
**not** refetch — the dedicated screen renders the state that's already there. The mechanism:
`ProfileScreen`'s three feature VMs are created with plain `hiltViewModel()` inside
`composable<NavRoute.Profile>`, so they're scoped to **Profile's own `NavBackStackEntry`**; the
dedicated screens then retrieve *that same instance* with
`hiltViewModel(remember(it) { navController.getBackStackEntry<NavRoute.Main>() })`. Those VMs live
in
`common/viewmodels/{favorites,history,statistics}/` (with their UiState files), and the three
*dedicated*
screens take a **required** `viewModel:` param with no `= hiltViewModel()` default — that's what
forces
the caller to hand them the shared instance instead of silently spinning up a second one.

**Why `NavRoute.Main` and not `NavRoute.Profile`** (this changed with the bottom-bar refactor —
see Bottom navigation below): Profile now lives on the **inner** tab `NavHost`, while
Favorites/History/Statistics live on the **outer** one, so `getBackStackEntry<NavRoute.Profile>()`
called on the outer controller would throw `IllegalArgumentException` — Profile simply isn't on that
back stack. `Main` is the one entry both hosts sit under, so scoping there is what makes the four
screens share one instance. Concretely: `MainScreen` declares them as
`favoritesViewModel: FavoritesViewModel = hiltViewModel()` params, which resolve against the
`NavRoute.Main` entry because that's where `MainScreen` is composed; the outer graph's
Favorites/History/Statistics destinations then pull the same instances back out via
`hiltViewModel(mainEntry)`. Each screen still runs its own `SideEffect { …updateUserId(…) }` —
harmless (the setters early-return when the value is unchanged) and it keeps each screen
self-healing.
The two old invariants this used to depend on (those three must not be tab items; "More »" must use
plain `navigateTo`) **no longer apply** — `Main` is never popped while a child screen is open.

By contrast `MangaSectionViewModel` is scoped to Home's own entry via a plain `hiltViewModel()`
default
on `HomeScreen` — it is **not** shared, and it cannot be scoped to `NavRoute.Splash` even though
Splash
precedes the tabs, because `navigateClearStack<Splash>(Main)` pops Splash `inclusive = true`,
destroying its `ViewModelStore`. Scope-to-a-parent-entry only works when that parent
provably stays on the back stack. A hub reuses each feature's VM **unchanged** — no new use case, no
repo/domain change, no
`limit` param: the VMs already fetch a 20-item first page, so "top N" is just `.take(N)` in the
section
composable. Per-section `*Section.kt` composables under `<screen>/components/sections/` own their
own
state (e.g. History's two-option navigate dialog) and take flat callbacks. **Every** block of a hub
is a
section in that folder, including the hub's own non-list content — Profile's
avatar/name/email/Update
block is `ProfileEditSection`, sitting next to `ProfileFavoritesSection`/`ProfileHistorySection`/
`ProfileStatisticsSection`, so `*Content.kt` stays pure assembly (sections + screen-level dialogs)
with
no layout details of its own. Spacing between sections comes from **one**
`Arrangement.spacedBy(16.dp)` on `*Content.kt`'s scrolling `Column` — sections themselves are passed
a
bare `Modifier.fillMaxWidth()`, so the page rhythm is set in exactly one place instead of being
re-derived per section. `ProfileFavoritesSection`/`ProfileHistorySection`/`ProfileStatisticsSection`
declare `onMoreClick: (() -> Unit)? = null` (optional, last param) to match `SectionHeader`; the
trade-off is that forgetting to wire it is no longer a compile error, the "More »" link just
silently
disappears. `ProfileEditSection` deliberately mirrors the horizontal shape of the now-deleted drawer
`MenuHeader` (avatar left at its intrinsic 80dp — `ProfilePicture` is a fixed
`size(80.dp)`, so **don't** give it a `weight`, that just strands it in an oversized slot — then
name/email stacked in a `weight(1f)` column, `spacedBy(16.dp)`), with the Update button below the
row.
`ProfileNameEdit` is `Arrangement.Start`-aligned for this reason: centered name next to a
start-aligned
email inside the same column reads as broken. Name and email intentionally **wrap** rather than
ellipsize — they carry the user's own identity, so truncating them is worse than an extra line.
**Section-level errors render inline** (`LoadPageErrorMessage`, which has a retry button) inside a
fixed-height `Box` — never a modal `AlertDialog`, because several sections load in parallel and
concurrent modals would stack on top of each other. Fixed-height section boxes also stop the page
from
jumping as each section resolves. **Section-level loading uses `ListLoadingIndicator`** (the slim
load-more bar from `common/indicators/`), **not `LoadingScreen`** — `LoadingScreen` is a
whole-screen
treatment and reads as "the page is loading" when it's really just one strip of it; with several
sections resolving independently you'd get multiple full-screen spinners stacked down the page.
Screen-level dialogs and `LoadingScreen` (the hub's own update/logout) stay as normal.

**`ListLoadingIndicator` call sites**: the indicator is a single `LinearProgressIndicator` at
`fillMaxWidth(0.4f)`, self-centered inside its own `Box` — so **never give it horizontal padding**
(it can't reach the edges; padding only shrinks the bar). It's also only ~4dp tall, unlike the text
states it shares a `when` with (`LoadMoreMessage`/`AllItemLoadedMessage`/`LoadPageErrorMessage`, ~
20dp),
so in a lazy list's load-more slot give it **vertical padding matching the `IDLE` branch at that
same
site** — otherwise the row collapses and the list visibly jumps the moment you tap "Load More".

**Shared ViewModels** (`presentation/screens/common/viewmodels/`): a ViewModel whose instance must
outlive a single screen lives here instead of under its owning screen's package. `NavGraph()` (zero
params, called as `setContent { NavGraph() }` from `MainActivity` — there is no `DexReaderApp.kt`
composable anymore) is the single composition root that instantiates every shared ViewModel via
`hiltViewModel()` and threads it down as a param — a screen never calls `hiltViewModel()` for one of
these itself. `viewmodels/onboarding/OnboardingViewModel` + `OnboardingUiState` gate the onboarding
screen (see
Onboarding above) — `NavGraph` both consumes it and passes it down.
`UserViewModel` (moved from top-level `presentation/`) exposes `isUserLoggedIn`/
`userProfile`, read by `NavGraph` and passed down as plain `isUserLoggedIn`/`currentUser` params to
every screen. `viewmodels/settings/SettingsViewModel` + `SettingsUiState` (moved from
`screens/settings/`, grouped under their own subpackage like `manga_section/` below) is read by
`NavGraph` to drive the app-wide `DexReaderTheme(themeOption = ...)` wrapping the whole `NavHost`,
and
that same instance is passed into `ProfileScreen(settingsViewModel = ...)` — both consumers share
one
instance instead of each calling its own `hiltViewModel()` (the original bug: `MainActivity` and the
old `SettingsScreen` each created an independent instance, so the two could desync). **There is no
Settings screen any more** — it was deleted and its only content (the theme picker) became
`ProfileSettingsSection` inside the Profile hub, so `NavRoute.Settings` and `MenuValue.SETTINGS` are
gone too; the tab bar is down to Home / Categories / Profile. `SettingsViewModel` itself is
untouched
and still lives in `common/viewmodels/settings/` because `NavGraph` needs `appliedThemeOption`
regardless of where the picker UI sits.
`viewmodels/manga_section/MangaSectionViewModel` + `MangaSectionUiState` (renamed from
`HomeViewModel`/`HomeUiState`, moved out of `screens/home/`) is instantiated once in `NavGraph` and
passed into `HomeScreen(viewModel = ...)` as a required param (no `= hiltViewModel()` default) — the
rename drops the Home-specific name so the same instance/type can be reused by other manga-listing
screens later.

**Full `uiState` vs. narrow flow at a wide-reach call site**: `NavGraph` collects
`settingsViewModel.uiState` directly (not a dedicated per-field flow) and reads
`.appliedThemeOption`
off it for `DexReaderTheme` — kept simple on purpose, since `isLoading`/`isSuccess`/`isError` only
churn while the user is already on the Settings screen (which is recomposing for that anyway), so a
narrow slice would avoid recomposition that has no real-world payoff here. `UserViewModel` still
exposes `isUserLoggedIn`/`userProfile` as two separate flows instead of one bundled state — that
split
earns its keep because those fields are read broadly across every screen, not just at `NavGraph`.
Only
reach for a narrow slice when the wide-reach call site's own churn is otherwise wasted; don't add
one
by default.

**Navigation**: `NavRoute` sealed interface with `@Serializable` members. `navigateClearStack()` for
auth flows; `navigatePreserveState()` for bottom-tab navigation. Value enums used as type-safe nav
args must be `@Serializable` (e.g. `MangaSortCriteriaValue`, carried on `NavRoute.CategoryDetails`).

**The two controllers live at different depths, and that difference is load-bearing.** The outer one
is a plain `val navController = rememberNavController()` at the top of `NavGraph()` and is passed
into every screen; the inner tab one is `rememberNavController()` **inside `MainScreen`**, i.e.
scoped to Main's own composition. Do **not** hoist the tab controller up next to the outer one in
`NavGraph()` (nor anywhere else that outlives Main): `NavHost` calls
`navController.setViewModelStore(viewModelStoreOwner.viewModelStore)`, whose implementation is
`if (viewModel == NavControllerViewModel.getInstance(store)) return; check(backQueue.isEmpty())`
(verified in `navigation-runtime` 2.9.8 sources, `NavControllerImpl.setViewModelStore`). The
`ViewModelStoreOwner` inside `composable<NavRoute.Main>` is Main's own `NavBackStackEntry`, so a
controller that outlives Main carries a non-empty `backQueue` into a **new** store the next time
Main is pushed and throws `IllegalStateException: ViewModelStore should be set before setGraph
call`. That is not hypothetical — it is the sign-in path: Profile → Sign In does
`navigateClearStack<NavRoute.Main>(NavRoute.Login)` (pops Main inclusive) and login success does
`navigateClearStack<NavRoute.Login>(NavRoute.Main)` (pushes a fresh Main). Keeping the `remember`
inside `MainScreen` ties the controller to Main's composition, so a re-pushed Main gets a fresh tab
controller and the tabs correctly restart at Home.

**Onboarding (`screens/onboarding/`)**: a 4-page `HorizontalPager` shown **once**, sitting between
Splash and Main on the outer host (`Splash → Onboarding → Main`, each hop via `navigateClearStack`,
so
Back never returns to it). Each page is one `OnboardingPageValue` entry
(`model/value/onboarding/`: `@param:DrawableRes imageRes` + `@param:StringRes titleRes` +
`descriptionRes` — same shape as `BottomTabItemValue`), rendered top-to-bottom as image → title
(`headlineMedium`) → description (`bodyLarge`). The page indicator and the Skip / Next / Get Started
row live **below** the pager in `OnboardingContent`, not inside `OnboardingPage`, so they stay put
while pages slide. The last page swaps Next for Get Started and drops Skip (`isLastPage` drives the
label, the button action, and Skip's visibility); Skip and Get Started both call the same
`onCompleteClick`.

The illustrations (`drawable/ob_discover|ob_browse|ob_read|ob_track.webp`, ~1000×1380 each) are
**real screenshots of this app** taken on the emulator, composited into phone mockups (rounded
corners + bezel + drop shadow; two overlapping phones on pages 2-4). When regenerating them: keep
the
composite's aspect ratio near the pager's image slot (**~0.7 w/h**) — a wider composite gets shrunk
by
`ContentScale.Fit` and leaves dead space above and below the phones, which is exactly what the first
pass looked like — and keep them **WebP** (the PNG originals were ~1.3 MB each, the WebP ~150 KB
with
no visible loss on these flat dark screenshots).

Plain `drawable/` (no density qualifier) is fine here and was **measured**, not assumed: entering
onboarding grows the native heap by ~5.5 MB with pages 1-2 composed, which matches a 1:1 decode
(4.5 + 5.4 MB). Compose's `painterResource` is not applying the mdpi→xxhdpi 3× upscale that a
`BitmapDrawable` would — at 3× a single page would cost ~49 MB on its own. Don't move these into
`drawable-xxhdpi/` on the theory that they need it.

**The "already seen it" flag lives in `SettingsRepository`**, not a new repository — same DataStore,
so `observeIsOnboardingCompleted()` / `saveIsOnboardingCompleted()` sit next to the theme pair and
need **no DI change** (`RepositoryModule` already binds it). `OnboardingViewModel`
(`common/viewmodels/onboarding/`) is a **shared** VM: `NavGraph` creates it, reads
`uiState.isCompleted` to route Splash, and hands the same instance to `OnboardingScreen`.
`OnboardingUiState.isCompleted` is `Boolean?` on purpose — `null` means the DataStore read hasn't
landed yet, and Splash routes to **Main** for anything that isn't an explicit `false`, so a slow or
failed read can never trap a returning user in onboarding (the VM's `onFailure` sets it to `true`
for
the same reason). There is no in-app reset: to see onboarding again during development, clear app
data (`adb shell pm clear com.decoutkhanqindev.dexreader`).

`SplashScreen` reads that flag — and its `navController` — through `rememberUpdatedState`, and that
is **load-bearing, not ceremony**: its `LaunchedEffect(Unit)` is composed before DataStore has
emitted, so a plain parameter capture would still be `null` three seconds later and every first-run
user would silently skip onboarding. Don't simplify those two `rememberUpdatedState` calls away.
(It used to be three — the two `onNavigateTo*Screen` lambdas collapsed into the single
`navController` when screens started navigating themselves.)

**App language (`util/LanguageManager.kt` + `screens/language/`)**: one stored value drives **both**
the UI locale and the MangaDex content language — it is
`SettingsRepository.observeContentLanguage()`
(a `MangaLanguage`).

**There is ONE language enum, `LanguageValue` (`model/value/language/`, 64 entries), used for
both the app UI locale and the MangaDex content language** — the separate `AppLanguageValue` was
**deleted**. It once mirrored every `LanguageValue` except `UNKNOWN` (wrapping each for
`code`/`flag`), but with `UNKNOWN` gone from both the domain `MangaLanguage` and `LanguageValue`
the two enums became a byte-identical 64-entry set with no reason to stay split. `LanguageValue`
now carries the picker helpers too — `DEFAULT` (= `ENGLISH`), `fromCode(code)`, and
`sortedForDisplay(deviceLanguageCode, displayIn)` — as companion members; the language picker
(`screens/language/`), `LanguageManager`, and `LanguageUiState`/`LanguageViewModel` all type on it
directly. `LanguageMapper` is down to `MangaLanguage.toLanguageValue()` /
`LanguageValue.toMangaLanguage()` (the two `AppLanguageValue` bridges are gone). **Adding a
MangaDex language is now a single edit** in `MangaLanguage` + `LanguageValue` — no second enum
to keep in step.

The picker lists all **64**, and **every one now ships a full UI translation** — `values/` (English)
plus **63** `values-XX/` folders = **64 UI locales**, i.e. **zero English-UI fallback left**. (
Android
resource fallback still means a code with no `values-XX/` would resolve every string from `values/`,
so a missing translation degrades gracefully rather than failing — that safety net just isn't
exercised any more now that coverage is complete.) The content language (titles, descriptions,
chapter feed) is driven off the same single stored value; a language change re-fetches content but
the UI locale switches from the shipped `values-XX/`.

`LanguageValue` no longer carries `@StringRes`: it carries `code` + `flag` (regional-indicator
emoji), and the display name comes from `Locale.forLanguageTag(code).getDisplayLanguage(...)` via
`LanguageManager.displayNameOf` / `labelOf`. That deleted all 65 `lang_*` string resources and makes
language names localize themselves — a Vietnamese UI shows "Tiếng Anh" without a single new string.

**`ProvideAppLanguage` must not override `LocalContext`.** The obvious recipe
(`LocalContext provides context.createConfigurationContext(config)`) **crashes this app**:
`createConfigurationContext` returns a plain `ContextImpl`, and every `hiltViewModel()` below the
provider then dies with `Expected an activity context for creating a HiltViewModelFactory`
(`MainScreen` creates three). Provide **`LocalResources`** (plus `LocalConfiguration` for
invalidation) instead — `stringResource` reads `LocalResources`, and the Activity context stays
intact for Hilt.

**63 `values-XX/` locales ship a full UI translation** (plus English `values/` = 64 total, matching
the 64-language picker exactly — **no fallback locales left**). Each carries all 148 translatable
strings (the two `translatable="false"` entries, `app_name` and `privacy_policy_url`, are correctly
absent everywhere). The **last 19 added** — `af`, `be`, `cv`, `eo`, `es-la` (folder `values-es-rLA`,
since `Locale.forLanguageTag("es-la")` → `es-LA`), `et`, `eu`, `ga`, `jv`, `ka`, `kk`, `la`, `lt`,
`lv`, `mn`, `ne`, `sr`, `tl`, `zh-hk` (folder `values-zh-rHK`) — were machine-translated in one
pass;
the low-resource ones (**`cv` Chuvash** especially, then `ka`, `kk`, `mn`, `ne`, `jv`, `la`) are the
first to hand to a native reviewer. Adding another language is still just a new
`values-XX/strings.xml`.

Two traps this resource set already sprang, both worth knowing before touching it again:

- **Locale folders must be direct children of `res/`.** These translations lived at
  `res/values/values-XX/` for a long time and were therefore compiled as nothing at all — Android
  never saw them. Only 43 of the original 72 were kept (those whose code MangaDex also supports);
  the 29 dropped were regional variants the picker cannot select (`en-rUS`, `es-rMX`, `ms-rMY`, …)
  or languages MangaDex has no code for (`gu`, `kn`, `ml`, `pa`, `xh`, `zu`, …).
- **A bare `'` in a string resource fails the build**, and AAPT2 reports it as
  `Invalid unicode escape sequence in string` with a line number pointing at an unrelated resource —
  in one case at an AndroidX library file in the Gradle cache. 61 unescaped apostrophes were hiding
  in these files (47 in Uzbek alone, where `o'qish`-style spellings are everywhere). Escape as `\'`.

Indonesian and Hebrew use the **legacy** folder names `values-in` and `values-iw`, not `values-id` /
`values-he`: `Locale.forLanguageTag("id").language` returns `"in"` in Java, and the app forces the
locale through a `Configuration` override, so the resource lookup uses the legacy code. Filipino is
three letters, so it needs the BCP-47 form `values-b+fil`.

Term choices worth keeping consistent if you add Vietnamese strings: Categories tab = "Danh mục"
while a genre = "Thể loại" (they must not collide), manga = "truyện", chapter = "chương",
volume = "tập".

**Content ViewModels refetch when the language changes.** Repositories read the preference *per
call*, so anything fetched before a change would otherwise keep the old language — most visible on
first run, where `MangaSectionViewModel` loads Home while the user is still on the language picker.
Five ViewModels therefore observe it and refetch: `MangaSectionViewModel`, `CategoriesViewModel`,
`CategoryDetailsViewModel`, `SearchViewModel`, `MangaDetailsViewModel`. The shape is always

```kotlin
observeContentLanguageUseCase().drop(1).collect { it.onSuccess { <refetch > () } }
```

**`.drop(1)` is load-bearing** — the DataStore flow replays its current value on collection, so
without it every one of these screens would fire a second fetch immediately on creation.
`SearchViewModel` additionally guards on a non-blank query (nothing to re-search otherwise), and
`MangaDetailsViewModel` re-runs `resolveChapterLanguageThenFetch()` as well as
`fetchMangaDetails()`,
since the chapter-language fallback has to be re-resolved for the new preference. Favorites/History
are deliberately **not** in the list: their titles are denormalised copies stored in Firestore, not
MangaDex responses, so a language change cannot affect them.

`LanguageTypeValue` (SELECTION/SETTING) is not cosmetic — it decides when Done enables:
`SELECTION` (first run, nothing applied yet) enables as soon as a language is tapped; `SETTING`
enables only when the tapped language *differs* from the applied one. Both screens render the same
`LanguageContent`; they differ only in top bar (Selection has no Back — the first-run flow must not
be escapable) and in that value.

**First-run order is `Splash → LanguageSelection → Onboarding → Main`**, gated by the *onboarding*
flag — there is no separate "language chosen" flag, so clearing app data replays both.

**Settings (`screens/settings/`)**: reached from a gear in **Profile's top bar**, not from a section
inside Profile (`ProfileSettingsSection`/`ThemeOptionItem` are deleted). `BaseScreen` grew
`isSettingsEnabled`/`onNavigateToSettingsScreen`; its right slot is search when `isSearchEnabled`,
else the gear, else nothing. Items come from `SettingItemValue` (THEME/LANGUAGE/PRIVACY) —
theme is a `Switch`, the other two navigate. **`ThemeMode` lost `SYSTEM`** (Light/Dark only), so
`DexReaderTheme` is now `themeOption == DARK` with no `isSystemInDarkTheme()`.

`PrivacyPolicyScreen` is a `WebView` in an `AndroidView` pointed at `R.string.privacy_policy_url`
(the GitHub Pages copy of `privacy-policy.html` at the repo root). JavaScript and DOM storage are
**off** — the page is static and doesn't need either.

**Bottom navigation — two `NavHost`s, one overlay bar**: there is no navigation drawer any more (the
whole `common/menu/` package is deleted). The app has **two nested back stacks**:

- **Outer** — `NavGraph()`'s `NavHost` (`startDestination = NavRoute.Splash`): `Splash`,
  `Onboarding`, the 3 auth routes, **`Main`**, and every screen reached *from* a tab
  (`MangaDetails`, `CategoryDetails`, `Search`, `Reader`, `Favorites`, `History`, `Statistics`).
- **Inner** — `screens/main/MainScreen.kt`'s own `NavHost` (`startDestination = NavRoute.Home`),
  driven by a `rememberNavController()` that must stay inside `MainScreen` — see Navigation above:
  exactly the 3 tabs, `Home` / `Categories` / `Profile`.

Everything on the outer host therefore has **no bottom bar for free** — no per-destination `if`
gating
the bar, because the bar is drawn inside `MainScreen` and disappears the moment the outer
controller navigates away. Add a new tab ⇒ inner host + a `BottomTabItemValue` entry; add a normal
screen ⇒ outer host, nothing else to touch.

`AppBottomBar` (`common/bottom_bar/`) is **deliberately not** Material3's `NavigationBar`: that
draws
an opaque container which would kill the scrim. It is a plain `Row` of 3 icon+label `Column`s
(`weight(1f)` each, `primary` when selected else `onSurfaceVariant`) and it owns **no** styling of
its own — it takes a bare `modifier`, and the scrim plus window insets are applied at the
`MainScreen`
call site, in this order: `blurBackground(alphas = persistentListOf(0f, 0.9f, 1f, 1f))` →
`navigationBarsPadding()` → `padding(horizontal = 16.dp, vertical = 8.dp)`. It is **not** in
`Scaffold.bottomBar` — it's `Modifier.align(Alignment.BottomCenter)` in the `Box` wrapping the inner
`NavHost`, so content scrolls *under* that gradient exactly like CategoryDetails' sort/filter
cluster
and MangaDetails' continue-reading/favorite cluster.
`blurBackground` is a gradient scrim, **not** `Modifier.blur` (API 31+, silently no-ops on
`minSdk = 24`) — see Compose Conventions. Because the bar is an overlay it does **not** push
content,
so each tab's scrollable content pays `bottom = 82.dp` for clearance (`HomeContent`'s sections
column,
`CategoriesGrid`'s `contentPadding`) — the same 82.dp `CategoryDetailsContent` already used to clear
its floating cluster. Profile's two bottom **buttons** (`LogoutButton` in `ProfileContent`,
`SignInButton` in `ProfileScreen`'s logged-out branch) instead both pay
`.padding(horizontal = 16.dp).padding(bottom = 78.dp)` — keep the two numbers identical so the
button
sits in the same place whether or not the user is signed in. **Any bottom-anchored *tappable*
element on a tab screen uses this same 78.dp**, including `CategoriesContent`'s `MoveToTopButton`
(`.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 78.dp)`). The number is derived, not
picked: `BaseScreen`'s `Scaffold` already applies the navigation-bar inset via
`.padding(paddingValues)`, so a tab's content region starts exactly where the bar's own
`navigationBarsPadding()` starts — meaning the bar occupies the first **~70dp** of that region
(`8` + `[6 + 24 icon + 2 + ~16 label + 6]` + `8`) and 78.dp clears it with an 8dp gap. Two
corollaries: **never add `navigationBarsPadding()` to such an element** (it would count the inset
twice), and don't reuse the 82.dp *content* clearance for something the user must tap — 82 happens
to clear the bar too, but 78 is the button convention and keeps them aligned across screens.

**Back is swallowed inside the tabs**: `MainScreen` declares a bare `BackHandler {}` — an empty
handler that consumes the event — so switching tabs is **tap-only**, Back never moves between them.
The handler is scoped to the `Main` composition, so it's disposed the moment the outer controller
navigates to a child screen: Back works normally on MangaDetails/Search/Reader/etc. and still
returns
you to the tabs. Consequence to keep in mind: Back on a tab root no longer exits the app either
(nothing is left to pop under `Main`) — Home is the only screen where that used to happen. If that
becomes unwanted, gate the handler on `selectedTab != BottomTabItemValue.HOME` rather than deleting
it.

The selected tab is derived from the inner controller, never stored:
`remember(currentBackStackEntry) { when { destination?.hasRoute(NavRoute.Categories::class) == true -> …
} }` — `NavDestination.hasRoute(KClass)` from
`androidx.navigation.NavDestination.Companion.hasRoute`.

`BottomTabItemValue` (`model/value/bottom_bar/`, renamed from `MenuValue`) has exactly 3 entries
carrying `nameRes` + `icon`; `BottomTabItemMapper.toNavRoute()` (renamed from `MenuMapper`) maps
them
to routes. The enum no longer needs an `isDrawerItem`/`drawerItems` split — everything in it *is* a
tab. Section headers that used to borrow `MenuValue.FAVORITES.icon`/`nameRes`
(`ProfileFavoritesSection`/`ProfileHistorySection`/`ProfileStatisticsSection`) now name their icon
and
string resource directly.

**Sign-in reachability**: the Sign In button used to live only in the drawer's `MenuHeader`. With
the
drawer gone, `ProfileScreen`'s logged-out branch owns it — `SignInButton`
(`profile/components/actions/`, next to `LogoutButton`, `ActionButton` + `colorScheme.primary` +
`Icons.AutoMirrored.Filled.Login`, and **no** confirm dialog since signing in isn't destructive)
sits
at exactly the Logout button's position under a `weight(1f)` `IdleScreen`. If you ever restructure
the
Profile screen, keep an entry point to Login there — the only other one in the app is MangaDetails'
"you must sign in to favorite" dialog. The drawer's credit line (`MenuFooter`) was **dropped
entirely**, not relocated — nothing renders `R.string.decoutkhanqindev` any more, so that string
resource is currently orphaned. Harmless, but decide it deliberately: either delete the string, or
re-add the line at the bottom of `ProfileContent` with the old
`bodySmall + Italic + onSurfaceVariant + centered` styling.

**Generalized manga-list browse (one path, optional tag)**: a Home section (Trending / Latest
Update /
New Release / Top Rated) is *just a preset sort criterion over the whole catalog with no tag
filter* —
the four section endpoints and the tag endpoint all hit the same `GET /manga`, differing only by
`includedTags[]`, and Retrofit drops a null `@Query`. So the browse is unified end-to-end on a
**nullable** tag: `ApiService.getMangaList(tagId: String? = null, …)` →
`CategoryRepository.getMangaList(categoryId: String? = null, …)` → `GetMangaListUseCase`
(`domain/usecase/manga/`, not `category/` — it's no longer category-specific).
`CategoryDetailsScreen`/
`CategoryDetailsViewModel` serve **both** entry points off `NavRoute.CategoryDetails(categoryTitle,
categoryId: String? = null, categoryDescription: String = "", initialSortCriteria = LATEST_UPDATE)`:
Categories/MangaDetails pass a
non-null `categoryId` (byte-identical to before); Home's per-section **"More »"** passes
`categoryId = null` + the section's mapped sort (`MangaSectionValue.toSortCriteriaValue()` in
`CriteriaMapper` — section→criteria is 1:1, with `NEW_RELEASE → MOST_VIEWED` since both are
createdAt-ordered). **The section browse's first page must match the Home row byte-for-byte**, so
the
VM seeds criteria differently per entry point: `categoryId == null` (section) →
`CategoryDetailsCriteriaUiState.forSection(sortCriteria)`, which reproduces the section endpoint's
exact
query — **no status + no content-rating filter** (empty `ImmutableList` ⇒ repo passes
`emptyList()` ⇒
Retrofit omits the `@Query` ⇒ MangaDex server default), except `LATEST_UPDATE` which seeds
`status = [ON_GOING]` (the one section endpoint that filters status). `categoryId != null` (
category)
keeps the plain `CategoryDetailsCriteriaUiState()` default (Ongoing + Safe). Do **not** give the
section
browse the category's Ongoing+Safe default — that was the original bug: it narrowed/reordered the
first
page so it no longer matched the Home row. Everything else (pagination/sort/filter machinery) is
shared
unchanged. When adding another "browse all manga sorted by X" surface, reuse this route — do **not**
add
a parallel use case/screen.

**Categories screen — genre cover-card grid**: `CategoriesContent` is a `LazyVerticalGrid`
(`GridCells.Fixed(2)`, full-span type headers via `item(span = { GridItemSpan(maxLineSpan) })` — the
`VerticalGridMangaList` pattern) grouped by `CategoryTypeValue` (Genre/Theme/Format/Content). Every
category is a `CategoryCard` built to match the **manga card** for app-wide consistency — same
`Card`
(`shapes.medium`, elevation 2, `surfaceVariant`) + `MangaCoverArt` (so the same
`R.drawable.placeholder`
shows on load/error) + `blurBackground` scrim, sized `fillMaxWidth × 250dp` like `MangaItem` — using
its
**#1 TRENDING cover** as a cropped background + name overlay (the Netflix/Webtoon "genre tile"). Do
not
re-hand-roll the cover/placeholder; reuse `MangaCoverArt`. The screen is for **browsing
many categories and picking one**, not consuming one — the single-category deep-dive is
`CategoryDetailsScreen`. (An earlier v1 stacked per-genre carousels/`HorizontalMangaList` here and
was
reverted precisely because each carousel was a mini-CategoryDetails, i.e. it focused on one category
at a
time; if tempted to add manga rows here again, don't — that's what CategoryDetails is for.) Each
card
**lazily** loads its cover when it composes (`LaunchedEffect(category.id) { onLoadCover(id) }`); the
VM
dedups (no-op if Loading/Success, re-fetch on Error). The cover fetch **reuses**
`GetMangaListUseCase(categoryId, limit = 1, sortCriteria = TRENDING, includeStats = false)` and
takes
`.firstOrNull()?.coverUrl` in the VM — `includeStats = false` skips the `MangaStatsRepository` merge
and
`limit = 1` fetches only the top-trending cover, so it's **one stats-less call, tiny payload** (no
separate cover use case — `GetMangaListUseCase` grew `limit`/`includeStats` params, defaulting to
the
old behavior so the browse callers are byte-identical). Covers live in a **separate** `categoryCoverStates:
StateFlow<ImmutableMap<String, CategoryCoverUiState>>` (Loading/not-yet-loaded → `shimmerLoading`
over the
`MangaCoverArt` placeholder; Success(coverUrl) → cropped cover; empty coverUrl or Error → the same
`R.drawable.placeholder` — a category is **never hidden**, always named + tappable). A card tap → `NavRoute.CategoryDetails(…, initialSortCriteria =
TRENDING)` so the detail's first item matches the card's cover. `CategoryRepository.getMangaList`
gained a
`limit: Int = 20` param, and `GetMangaListUseCase` gained `limit`/`includeStats` params, for this.

### State Management

**Error dialog state**: `remember { mutableStateOf(false) }` +
`SideEffect(uiState) { if (uiState is Error) isShowErrorDialog = true }` — never key `remember` on
`uiState` (or a field of it) to derive the initial/show value; only the keyed effect may set it back
to
`true`, so dismiss stays dismissed until the tracked condition flips again. For bundled (non-sealed)
UiState, key the effect on the specific boolean field (`uiState.isError`), not the whole state
object,
so unrelated field changes (e.g. text input) don't re-arm the dialog. Uses keyed `SideEffect`
(`compose-runtime 1.12.0+`, via `composeBom 2026.08.00`), not `LaunchedEffect` — the body here is
always a synchronous state-flag flip or a plain (non-suspend) ViewModel setter call, never a
coroutine/suspend call, so the coroutine `LaunchedEffect` launches is pure overhead. This is also
the
pattern for the `LaunchedEffect(isUserLoggedIn, currentUser?.id) { viewModel.updateUserId(...) }`
family at the top of most `*Screen.kt` files, and one-shot side-effect calls like `CategoryCard`'s
`LaunchedEffect(category.id) { onLoadCover(category.id) }` — same reasoning, same fix, same file.
**Keep `LaunchedEffect` when the body does any suspend work** — `delay()`, `Animatable.animateTo()`/
`.snapTo()`, `.collect()` on a flow, or calling a `suspend fun` — `SideEffect`'s `effect` is
`() -> Unit` with no `CoroutineScope`, so it cannot do any of that (`AnimatedLogoAndSlogan`,
`SplashScreen`, `MangaBanner`'s two pager-animation effects, `ReadingActivityChart`'s
`modelProducer.runTransaction { }` are all `LaunchedEffect` for exactly this reason, not
oversights).
`SideEffect` also runs earlier than `LaunchedEffect`/`DisposableEffect` in the frame (during
composition's apply-changes phase, not dispatched to a coroutine) — fine for the state-flag/setter
pattern above, but worth re-checking case-by-case for anything timing-sensitive before converting.

**Staged vs. applied value for a wide-reach effect**: when a field is both (a) reflected immediately
in
a screen's own UI and (b) drives a wider-reach effect that only takes hold once persisted, split it
into
a staged field (updates the moment the user taps, for in-screen feedback) and an applied field (
updates
only after the write succeeds) — never let one field serve both roles.
`SettingsUiState.selectedThemeOption` (tapped option, drives the radio highlight in
`ProfileSettingsSection`) vs. `appliedThemeOption` (persisted value, read by `NavGraph` to drive
`DexReaderTheme` — see Screen Structure) is the established example. Theme now **applies on tap**:
`ProfileScreen` calls `updateThemeOption(it)` then `saveThemeOption()` back to back (safe — the
former
is a synchronous `MutableStateFlow.update`, so the latter reads the new staged value). There is
deliberately **no confirm dialog and no success dialog** any more: a theme switch inside a profile
page
is a toggle, not a commitment, and the app repainting is its own confirmation — two modals to flip a
theme was the old Settings-screen behaviour and it did not survive the move. `resetThemeOption()`
still
earns its keep: on a failed write it snaps `selectedThemeOption` back to `appliedThemeOption` so the
selection can't sit on a value that was never saved. The write error surfaces inline via
`LoadPageErrorMessage` with retry, matching the rest of the hub's sections. The picker itself is a
`Row` of three `ThemeOptionItem`s at `weight(1f)` each (icon + short label, centred) — the labels
are
deliberately one word (`R.string.light`/`dark` are "Light"/"Dark", not "Light Mode"/"Dark Mode") so
three fit across a phone without truncating.

**Forcing a same-screen `HorizontalPager` jump from the ViewModel**:
`rememberPagerState(initialPage = ...)` only reads `initialPage` once — there's no built-in channel
for the ViewModel to move the pager afterward. `ChapterPagesSection` (Reader) reports the pager's
position outward via `LaunchedEffect(pagerState.currentPage) { onUpdateChapterPage(...) }` but has
no
listener for external page changes. To force a jump within the same chapter (e.g.
`ReaderViewModel.resetChapterProgress()` snapping back to page 1), reuse the same unmount/remount
trick `navigateToPreviousChapter()`/`navigateToNextChapter()` already rely on: set
`_chapterPagesUiState.value = ChapterPagesUiState.Loading` (unmounts `ChapterPagesSection` —
`ReaderContent`'s `when` branch doesn't call it during `Loading`), then immediately set a new
`Success` with the target page — `rememberPagerState` re-reads `initialPage` fresh on remount.
`updateChapterPage()` alone cannot do this: it only `.copy()`s an existing `Success` and never
assigns
`Loading`, so it never triggers the remount.

**Cancel in-flight mutation jobs before navigating away**: any `viewModelScope.launch` that captures
a
`ChapterPagesUiState.Success` snapshot before a suspension point (e.g. a Firestore call) must be
tracked as a cancellable `Job` and cancelled at the top of any function that can change
`currentChapterId` mid-flight — otherwise the mutation's `.onSuccess` can fire later using the stale
snapshot, unmounting the *new* chapter's pager and resurrecting the *old* chapter's pages under the
new chapter's id. `ReaderViewModel.resetProgressJob` (cancelled, with `_resetProgressUiState` reset
to
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
  for loading/dim overlays — use `Modifier.blurBackground(alphas, …)` (gradient, works on
  every API level, cheaper), the established pattern across every screen (Login, Register,
  ForgotPassword, History, Profile, MangaDetails, CategoryDetails, Home banner, manga/category
  cards,
  `AppBottomBar`)
- `Modifier.blurBackground(alphas: ImmutableList<Float>, color, startY, endY)` takes the gradient as
  an **arbitrary-length alpha list**, one entry per evenly-spaced stop —
  `blurBackground(alphas = persistentListOf(0f, 0.1f, 0.8f, 1f))`. It used to be four fixed params
  (`topAlpha`/`topCenterAlpha`/`bottomCenterAlpha`/`bottomAlpha`, the two center ones nullable and
  defaulting to their neighbour); that shape could not express 2, 3, or 5+ stops and made every
  call site reason about which slot fell back to which. **Don't reintroduce named stop params.**
  Two things to keep in mind: the list is a `Brush.verticalGradient` colors list, so **it needs at
  least 2 entries** (one throws `IllegalArgumentException: colors must have length of at least 2`),
  and stop *count* changes the curve — the old `topAlpha = 0f, bottomAlpha = 1f` expanded to
  `[0, 0, 1, 1]` (flat, then a ramp across the middle third, then flat), **not** a straight `[0, 1]`
  ramp, which is why the overlay call sites (`SortAndFilterSection`, `ReadingAndFavoriteSection`)
  spell out all four. `alphas` has no default — every call site states its own gradient.
- Coil `ImageRequest` passed to `AsyncImage` / `ZoomableAsyncImage` must be
  `remember(url) { ImageRequest.Builder(...).build() }` — never built inline in the call site.
  Established in `MangaCoverArt`, `ChapterPageImage`, `MangaDetailsBackground`, `ProfilePicture`
- **The Coil singleton `ImageLoader` is configured in `App`** (`App : Application(),
  SingletonImageLoader.Factory`), not per call site — Coil 3 resolves it via
  `(applicationContext as? Factory)?.newImageLoader(...)`, so `AsyncImage`, `ZoomableAsyncImage`
  (telephoto) and every other Coil entry point all pick it up with no call-site change. The
  overrides are **deliberate deviations from measured Coil 3 defaults**, not cargo cult — the
  defaults (read from `coil-core` sources) are memory `0.2 × totalAvailableMemory` (`0.15` on
  low-RAM), disk `2%` of free space clamped to `[10 MB, 250 MB]` at
  `SYSTEM_TEMPORARY_DIRECTORY/coil3_disk_cache` (= app `cacheDir` on Android, since Android sets
  `java.io.tmpdir` to it). This app raises memory to `0.25` and disk to `5%` clamped
  `[64 MB, 512 MB]`, because one manga chapter is 20-40 full-page images — a 10 MB floor cannot
  hold even one, so re-reading refetched everything. **The directory name `coil3_disk_cache` is
  kept identical to Coil's own default on purpose**: `DiskCache.Builder` requires an explicit
  `directory()`, and reusing the default path means existing users keep their cached pages across
  the update instead of silently re-downloading. Disk size is a **storage trade-off**, not a free
  win — lower it if users complain about app size. The per-request
  `.memoryCachePolicy(ENABLED)/.diskCachePolicy(ENABLED)` calls in `MangaCoverArt`/`ChapterPageImage`
  /`ProfilePicture` are redundant (they restate Coil defaults) but harmless and left as-is
- **No `.crossfade(...)` anywhere** — not on the singleton loader, not on any `ImageRequest`. Coil's
  own default is crossfade **off**, so the absence is the configuration; do not "restore" it. Image
  loading is signalled by `Modifier.shimmerLoading(isEnable = !isImageLoaded)` at the call site
  instead (`MangaItem`, `FavoriteMangaItem`, `ReadingHistoryItem`, `ProfileHistoryItem`,
  `MangaBanner`, `CategoryCard` around `MangaCoverArt`; `ChapterPageImage` around its own image) —
  running a fade *and* a shimmer for the same load is two competing transitions, and
  `MangaCoverArt` was doing it at **800 ms**, so a fast scroll through a grid stacked dozens of
  simultaneous alpha animations on top of shimmer. `ProfilePicture` and `MangaDetailsBackground`
  have **no** shimmer and now pop in with no transition at all — that is a deliberate, accepted
  trade-off (uniform "no crossfade" rule beats a per-file exception), not an oversight
- Shared manga-info atoms live in `presentation/screens/common/badges/`: `MangaStatusBadge` (icon +
  label from `MangaStatusValue`), `MangaRatingChip` (star + rating), `MangaGenreChip` (label +
  optional `onClick`). All three share one visual family — `primaryContainer.copy(alpha = 0.9f)`
  pill, `shapes.small`, `tonalElevation = 4.dp` + `shadowElevation = 4.dp` — so badges/chips shown
  together never end up with mismatched elevation. Reuse these wherever manga status/rating/genre is
  shown instead of hand-rolling a new `Surface` or `Card` — established in `MangaItem`,
  `FavoriteMangaItem`, `MangaBanner`, `MangaInfoSection`, `MangaCategoryList` (Manga Details),
  `CategoryList` (Categories screen)
- `SectionHeader` (`presentation/screens/common/sections/`) — shared "icon + title + More »" row for
  any
  section of a bigger screen. Signature:
  `SectionHeader(icon: ImageVector, title: String, modifier, onMoreClick: (() -> Unit)? = null)`. It
  owns
  no padding — every call site passes `Modifier.fillMaxWidth().padding(start/end = 16.dp, top = 8.dp,
  bottom = 4.dp)`, matching how section spacing lives at the call site everywhere else. If you add a
  new
  caller, copy that padding; forgetting it is not a compile error, the header just sits flush
  against the
  screen edge while every other section is inset. **`onMoreClick` is nullable on purpose**: pass it
  when the section has a
  dedicated full screen behind it (Home's rows, Profile's Favorites/History/Statistics), leave it
  out
  when the section *is* the whole feature and there is nowhere to go — `ProfileSettingsSection`
  omits it,
  and the "More »" row simply isn't rendered. Never re-hand-roll this row — the "More »" affordance
  must
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
  chapter list), `ReadingHistoryInfo` (history list), and `NavigateChapterBottomBar` (Reader's
  bottom
  bar center slot, between the prev/next chapter `IconButton`s, via `Modifier.weight(2f)`) so all
  three show identical progress info instead of some being percent-only and others page-count-only.
  M3 1.4's `LinearProgressIndicator` defaults to the "expressive" style (a gap near the end + a
  small
  stop-indicator dot) — pass `gapSize = 0.dp` and `drawStopIndicator = {}` to get the classic
  continuous bar needed for a compact list-row indicator
- `AppTopBar` (`presentation/screens/common/top_bars/`) — single composable replacing the former
  `MainTopBar`/`DetailsTopBar` pair; serves both `BaseScreen`'s tab-root variant (title + optional
  search icon, **no left slot** now that the hamburger is gone) and
  `BaseDetailsScreen`'s/`ReaderScreen`'s Back variant. Three independent slots — `center`/`left`/
  `right` — each resolved via `when { xContent != null -> xContent(); xIcon != null -> ...; xTitle
  != null -> ... }`: `xContent: (@Composable () -> Unit)?` wins if set (`ReaderScreen`'s
  volume/chapter/title `centerContent`), else `xIcon: ImageVector?` renders as a `Box(Modifier.
  onClick(shape = CircleShape) { ... }) { Icon(...) }` (not a plain `IconButton`), else `xTitle:
  String?` renders as `Box(Modifier.onClick { onXClick() }) { Text(...) }` — `centerTitle` uses
  `titleLarge` (no weight override needed, see the typography-system bullet below), `leftTitle`/
  `rightTitle` use `bodyMedium + ExtraBold` but currently have no real caller (every screen only
  ever
  passes `leftIcon`/`rightIcon`/`centerContent`, never `leftTitle`/`rightTitle` — a
  dead-but-harmless
  path, not urgent to remove). All three slots default to `null`/no-op — a slot's presence is its
  own visibility switch, no separate `isEnabled`-style boolean (e.g. `rightIcon = if
  (isSearchEnabled) Icons.Default.Search else null`). Both `Icon(...)` calls hardcode
  `contentDescription = null`. Colors are 4 flat `Color` params (`containerColor`,
  `centerContentColor`, `leftContentColor`, `rightContentColor`) instead of a bundled
  `TopAppBarColors`, defaulting to the Back-variant look (`surfaceContainer`/`onPrimaryContainer`);
  `BaseScreen`'s tab-root variant overrides all four (transparent container, `onSurface` title,
  `primary` search icon). The translucent `Surface(surface.copy(alpha = 0.95f),
  tonalElevation = 3.dp)` is **inside `AppTopBar` itself** (`AppTopBar.kt:50`), so every caller gets
  it — an earlier version of this doc wrongly described it as a `BaseScreen` call-site wrapper. 5
  screens sit behind `BaseDetailsScreen` (`MangaDetailsScreen`, `CategoryDetailsScreen`, plus
  `FavoritesScreen`/`HistoryScreen`/`StatisticsScreen`) — **which wrapper a screen uses follows
  directly from which `NavHost` it lives on**: `BaseScreen` (title + search, no back) only for the 3
  inner-host tabs (Home, Categories, Profile); `BaseDetailsScreen` (Back icon) for everything on the
  outer host, i.e. everything reached *from* a tab. Moving a screen across this line also drops its
  tab-only params and adds `onNavigateBack`. `BaseScreen` itself is now just a `Scaffold` +
  `AppTopBar` + content `Box` — it has no `bottomBar` slot (the bottom bar is an overlay owned by
  `MainScreen`, not a Scaffold slot) and no `isUserLoggedIn`/`currentUser`/
  `onNavigateToSignInScreen`
  params (those were drawer-header concerns). `ForgotPasswordScreen`/`RegisterScreen` have no top
  bar at all (just `BackHandler`
    + a `*Content` call), despite what an earlier version of this doc claimed. `ReaderScreen` calls
      `AppTopBar`
      directly (not through `BaseDetailsScreen`, since it also needs a `bottomBar`/FAB/full-screen
      `AnimatedVisibility` toggle that `BaseDetailsScreen` doesn't expose): `centerContent` renders
      the
      volume/chapter number + chapter title (moved here from `NavigateChapterBottomBar`'s center
      slot —
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
  SemiBold); chapter-row flavor text (the separator dot, chapter title in list rows, `ReaderScreen`'
  s
  top-bar subtitle) uses `labelMedium + fontStyle = Italic`; footer/quiet-caption text
  ("Don't have an account?") uses its own base style + `fontStyle = Italic` +
  `color = onSurfaceVariant`, no weight override — this is distinct from a *clickable* inline link
  (`LoginForm`'s "Forgot Password?"/"Sign Up", `titleMedium + Bold + Italic + onPrimaryContainer`),
  which signals tappability via color and must not be folded into the quiet-caption convention. The
  Favorite/Unfavorite button's background is `FavoriteRed` (`presentation/theme/Color.kt`, alongside
  the existing `RatingStarGold`) — a deliberate brand color, not theme-adaptive by design,
  formalized
  from an inline hex literal rather than switched to `colorScheme.error`.

### Compose Performance

- **Don't wrap composable callbacks in `remember`.** Strong skipping (Kotlin 2.3.x, default-on since
  Compose compiler 2.0.20 — nothing in `app/build.gradle.kts`'s `composeCompiler {}` disables it)
  auto-memoizes lambdas **and** method references with unstable captures (like a `viewModel`) at a
  `@Composable` call site, so `remember { viewModel::method }` is redundant. Pass a plain
  `{ viewModel.method() }` lambda instead (audited: all ~51 sites across every `*Screen.kt` use this
  form, not `remember { viewModel::method }` — a lambda literal is *unambiguously* memoized,
  sidestepping
  any lingering method-reference doubt). **Scope matters — two things this does NOT cover, which
  still
  need `remember`:** (1) lambdas inside `LazyListScope.items { }` (non-`@Composable` — see the
  `items`
  bullet below); (2) `remember(key) { expensiveComputation() }` /
  `remember { mutableStateOf(...) }` /
  `remember { derivedStateOf { } }`, which are caching/state, not lambda memoization — always keep
  those.
- LazyList keys: never include index — stable server-side ID only
- `contentType` is only worth adding to a **mixed** feed — a lazy layout whose slots hold
  structurally different composables — so the layout reuses a slot for the same kind of item
  instead of tearing down and re-composing. `CategoriesGrid` is the one real case (4 full-span
  `Text` headers interleaved with 250dp `CategoryCard`s) and uses a `private enum class
  CategoriesGridContentType { HEADER, CARD }` declared at the top of that file — a private enum,
  not a `String` (typo-proof) and not a top-level `private val` (which the Comments/Compose rules
  forbid). Homogeneous lists (`HorizontalMangaList`, `FavoritesContent`, `ReadingHistoryList`) do
  **not** need it; a list whose only extra slot is a single header or load-more footer gains
  nothing measurable, so don't add it there by reflex
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
  (`remember(item.id) { mutableStateOf(...) }`) — never hoisted above the pager/list. Hoisting
  shares
  one instance across every page/item (`MangaBanner` bug: one shared flag turned off shimmer on
  every
  banner page as soon as a single image finished loading, and re-triggered recomposition of every
  composed page on each toggle)
- Custom animation modifiers that drive `graphicsLayer { }` or `drawWithContent { }` must read the
  animated `State<Float>` via `.value` **inside** that deferred block — never destructure via `by`
  at the top of the function. A `by` read there re-triggers full recomposition on every animation
  frame instead of a cheap redraw/relayout-only pass. `onClick`, `shimmerLoading` and
  `shimmerHighlight` in `common/Modifiers.kt` all follow this correctly — use them as the
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
  `padding(horizontal = 16.dp, vertical = 6.dp)` rendered as 48dp/18dp, `MangaItem`'s
  `padding(4.dp)`
  as 12dp — which is what the "UI bị padding thêm" reports across Home/Favorites/History/Search/
  CategoryDetails/MangaDetails actually were. `shimmerLoading`/`shimmerHighlight` always used the
  `Modifier` companion and were never affected. When a shared modifier is suspected, read its
  `.then()` receivers first — no padding literal changes, so grepping diffs for `padding`/`spacedBy`
  will not find it.
- **There is no per-item entrance animation, and adding one back needs a very good reason.**
  `Modifier.animateItemOnAppear()` (a `MutableTransitionState` + `rememberTransition` driving
  `alpha` + `translationY` through `graphicsLayer`) was **deleted** from `common/Modifiers.kt`
  along with all 7 call sites (`MangaItem`, `FavoriteMangaItem`, `ReadingHistoryItem`,
  `ProfileHistoryItem`, `MangaChapterItem`, `CategoryCard`, `MangaBanner`) because it made every
  list measurably worse. Its cost was structural, not a tuning problem: it is `@Composable`, so
  **each item paid a `Transition` plus two `animateFloat` animation objects**, and Compose keeps a
  frame callback running for every unfinished transition — scroll fast through a grid and dozens
  run at once, on top of each item's image decode and shimmer. Worse, the entrance **replays every
  time an item scrolls back into view** (a LazyList re-composes items it reuses, and `remember { }`
  inside the modifier cannot survive that), so it was never actually a once-per-item "on appear"
  effect — it was a permanent per-scroll tax that also made items visibly fade in again on the way
  back up. If per-item entrance animation is ever wanted again, use `Modifier.animateItem()` from
  Compose Foundation (the built-in, which handles placement/appearance/disappearance at the lazy
  layout level instead of per-composable) rather than re-hand-rolling this.
- **Never animate `scaleX`/`scaleY` in a list/grid item's entrance animation** if one is ever
  reintroduced. A `graphicsLayer` scale does not shrink the layout slot — only the drawn content —
  so a mid-animation item renders small inside a full-size slot and reads as extra padding around
  every item. That is exactly why the deleted modifier above animated only `alpha` +
  `translationY`. Transient scale is fine for press feedback (`onClick`), where the element is not
  one of many in a list.
- `MangaBanner`'s pager applies `scale`/`alpha` = `lerp(0.92f/0.6f, 1f, 1f - pageOffset)` per page
  in `graphicsLayer` — a banner screenshotted mid-auto-scroll is legitimately ~0.92× size and
  dimmer.
  Same caveat as above: judge banner spacing only from a settled pager, never mid-transition

---

## Maintenance

**Whenever architecture, build setup, or any layer's patterns change, this CLAUDE.md must be updated
in the same session.** This includes: new layers/modules, build command changes, exception hierarchy
changes, Hilt module changes, new utilities, use case/error-handling pattern changes, new
screens/composables/UiState/mappers/NavRoutes/Value enums.

**Large multi-file sessions** (e.g. a codebase-wide audit) log a dated entry in `CHANGELOG.md` at
the
repo root — date, title, what changed. Append new entries there rather than creating new one-off
report files per session.
