# Changelog

Dated log of notable multi-file / cross-cutting work sessions. Newest entry first.

---

## 2026-07-12 — Fix shimmer bleeding into a square instead of following its shape

`shimmerLoading`/`shimmerHighlight` (from the migration below) draw their gradient sweep with
`DrawScope.drawRect(brush = ...)`, which always fills the full rectangular draw bounds of the node —
it has no awareness of the actual (possibly circular/transparent) visual silhouette of the content
underneath. Any call site without its own external `.clip()` positioned *after* the shimmer modifier
in the chain showed the sweep bleeding out as a visible square/rectangle around a round icon —
reported by the user via a screenshot of the Login screen logo.

**Root cause confirmed via Compose's modifier-chain draw-ordering semantics** (earlier modifiers
wrap/constrain later ones): `AnimatedLogoAndSlogan.kt` (Splash) and `AuthHeader.kt`
(Login/Register/ForgotPassword) had no clip at all — the exact bug in the report. `ListLoadingIndicator.kt`
had the same gap, less noticeable at 36dp. `MangaBanner.kt` had a `.clip()` but positioned *after*
`shimmerLoading` in the chain, so it never bounded the shimmer's own draw. `MangaItem.kt` /
`FavoriteMangaItem.kt` / `ReadingHistoryItem.kt` were safe as-is — their ancestor `Card`'s shape-clip
already hierarchically bounds everything drawn inside it. `LoadingScreen.kt` had already been fixed
independently (`.clip(CircleShape).shimmerLoading()`).

**Fix:** added a `shape: Shape? = null` param to both `shimmerLoading` and `shimmerHighlight` in
`common/Modifiers.kt`, applying `.clip(shape)` as part of each modifier's own self-contained chain,
right before its `drawWithContent { }`. Updated call sites: `AnimatedLogoAndSlogan.kt` and
`AuthHeader.kt` → `shape = CircleShape`; `ListLoadingIndicator.kt` → `shape = CircleShape`;
`MangaBanner.kt` → `shape = MaterialTheme.shapes.medium` passed directly to `shimmerLoading`, removing
the now-redundant standalone `.clip(MaterialTheme.shapes.medium)` line (and its now-unused `clip`
import) that used to sit after it in the chain.

**Verified:** `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL, then confirmed visually on-device
(`Pixel_10_Pro_XL` emulator) — Login screen logo now shows a clean circular glow tightly following the
icon's silhouette, no square bleed.

**Cleanup:** removed the stray `Modifier.kt~` backup file left over from the `Modifier.kt` →
`Modifiers.kt` rename (untracked editor backup, not part of the Gradle source set).

---

## 2026-07-12 — Modifier consolidation: onClick, shimmerLoading, shimmerHighlight

Replaced `Modifier.onScalableClick` and `Modifier.shimmer` (backed by the third-party
`com.valentinilk:shimmer` library) with three new primitives in `common/Modifier.kt`:

- `Modifier.onClick(shape = CircleShape, ripple = true, action)` — rewritten with `composed { }` +
  manual `pointerInput`/`awaitEachGesture` press detection instead of
  `MutableInteractionSource.collectIsPressedAsState()`. Default shape changed from `null` (no clip)
  to `CircleShape`.
- `Modifier.shimmerLoading(isEnable = true, durationMillis = 1000)` — hand-drawn diagonal gradient
  sweep via `drawWithContent { drawContent(); drawShimmerGradient(...) }`, no external dependency.
  Same `isEnable` toggle as the old `shimmer()`.
- `Modifier.shimmerHighlight(backgroundColor, highlightColor, durationMillis = 1400)` — same gradient
  sweep but drawn `drawRect(backgroundColor); gradient; drawContent()`, i.e. behind the content
  instead of on top. No `isEnable` toggle — always on while composed.

**onClick migration — the shape default change was the risk.** 6 call sites already passed an
explicit shape (mechanical rename). 8 call sites relied on the old implicit `null` (no clip) and
would have been silently clipped into a circle by the new default — added explicit
`shape = RectangleShape` at each to preserve the original look: `LoginForm.kt` (forgot-password and
sign-up text links), `LoadMoreMessage.kt`, `MangaDescription.kt`, `ThemeOptionItem.kt` (also renamed
its `block =` named argument to `action =`), `MangaChaptersHeader.kt`,
`ChapterLanguageListBottomSheet.kt`, `CategoryTypeHeader.kt`.

**shimmer migration — split by semantic intent, not by draw mechanics.** Determined for each call
site whether the shimmer represents a genuine loading/fetch state or a permanent decorative flourish:

- `shimmerLoading` (6 sites, all keep `isEnable` tied to a real loading condition or *are* the
  loading indicator itself): `LoadingScreen.kt`, `ListLoadingIndicator.kt`, `MangaItem.kt`,
  `FavoriteMangaItem.kt`, `ReadingHistoryItem.kt`, `MangaBanner.kt` (all `isEnable = !isImageLoaded`
  except `LoadingScreen`/`ListLoadingIndicator`, which have no toggle since the component itself only
  renders while loading).
- `shimmerHighlight` (2 sites, always-on branding effect unrelated to any fetch state):
  `AnimatedLogoAndSlogan.kt` (Splash) and `AuthHeader.kt` (Login/Register/ForgotPassword) — both
  shimmer the app logo continuously regardless of load state. Used `backgroundColor = Transparent`
  + a theme-derived `highlightColor` (`onBackground`/`onSurface` at 0.35 alpha, matching the old
  hardcoded white-alpha convention) since the new function requires both explicitly. Preserved
  `AuthHeader`'s explicit 1800ms duration; let `AnimatedLogoAndSlogan` fall back to the new
  1400ms default since the old code never set one either.

**Verified:** `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL after all 21 call sites migrated.

**Follow-up (same day):** fixed the phase-deferral regression in all three new animated modifiers —
`onClick` read `scale` via `by animateFloatAsState(...)` before `graphicsLayer { }`, and both
`shimmerLoading`/`shimmerHighlight` read `progress` via `by transition.animateFloat(...)` before
`drawWithContent { }`, all outside the deferred block. Since the shimmer modifiers run an infinite
animation for as long as they're composed (every loading `MangaItem`/`FavoriteMangaItem`/
`ReadingHistoryItem`/`MangaBanner` cover, plus the two logo highlights), this was recomposing every
composed instance on every animation frame instead of only redrawing. Fixed by keeping `scale`/
`progress` as plain `State<Float>` and reading `.value` inside the `graphicsLayer { }` /
`drawWithContent { }` block instead. Verified with `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL.
File was also renamed `Modifier.kt` → `Modifiers.kt` during this session; `CLAUDE.md` updated to
match.

**Open item:** `libs.compose.shimmer` (`com.valentinilk:shimmer`) in `app/build.gradle.kts` is no
longer imported anywhere in the codebase — candidate for removal in a follow-up, not done here since
it's a build-file change outside this migration's scope.

---

## 2026-07-12 — Compose performance audit (all features + common/)

Full code-level Diagnose → Fix pass across every feature screen (Auth, Categories, Category Details,
Favorites, History, Home, Manga Details, Profile, Reader, Search, Settings, Splash, Statistics) plus
the shared `presentation/screens/common/` component library. No physical device was available this
session, so this was a code-level audit, not a Macrobenchmark-measured one — see Open items.

**Cross-cutting patterns fixed** (each showed up in more than one unrelated feature):

- `Modifier.blur(8.dp)` used for loading/dim overlays — no-op below API 31 (app `minSdk = 24`), and a
  heavier hardware layer than the alternative even above it. Fixed in `LoginContent.kt`,
  `HistoryContent.kt`, `ProfileContent.kt` → replaced with `Modifier.blurBackground(topAlpha,
  bottomAlpha)`.
- Per-item callback lambda built fresh inside a plain `forEach` inside a composable, without
  `remember(key)` — tapping one item forced every sibling in the loop to recompose too. Fixed in
  `CategoriesContent.kt` (`onExpandClick`) and `FilterValueOptions.kt` (checkbox `onClick`).
- State scoped to one page of a `HorizontalPager` hoisted above the content lambda instead of inside
  it. Fixed in `MangaBanner.kt` — `isImageLoaded` was shared by every banner page, so the first image
  to load switched off shimmer everywhere.
- Animated `State<Float>` read via `by` delegate at the top of a `graphicsLayer`-driven modifier
  function instead of via `.value` inside the `graphicsLayer { }` block — caused full recomposition
  every animation frame instead of a redraw-only pass. Fixed in `common/Modifier.kt`'s
  `animateItemOnAppear()` and `onScalableClick()` (also swapped deprecated `updateTransition()` for
  `rememberTransition()`).

**Other fixes:**

- `SuggestionList.kt`: `key = { it.hashCode() }` risked a duplicate-key crash since suggestion
  strings aren't guaranteed unique (two different manga can share a title). Corrected to omit `key`
  entirely rather than key on the value itself — the list is always replaced wholesale per debounced
  query, so there's no reorder/insert identity to preserve anyway.
- `ProfilePicture.kt`: Coil `ImageRequest` was built inline every recomposition instead of
  `remember(url) { ... }`, unlike `MangaCoverArt` / `ChapterPageImage` / `MangaDetailsBackground`.

**Explored and reverted:** tried migrating `MangaItem`/`FavoriteMangaItem`/`ReadingHistoryItem`/
`MangaChapterItem` from the hand-rolled `animateItemOnAppear()` to Compose's native
`LazyGridItemScope`/`LazyItemScope.animateItem()`. Rolled back after confirming `animateItem()` only
animates genuine list mutations (insert/remove/reorder), not "item enters view for the first time,"
which is what these screens actually want — kept `animateItemOnAppear()` and optimized it instead
(see above).

**No bugs found** (verified clean): Manga Details, Favorites, Reader, Settings, Splash, Statistics.

**`CLAUDE.md` updated** — added the `blur()` vs `blurBackground()` rule, the LazyList-key uniqueness
exception, the Coil `ImageRequest` remember rule, the per-page/per-item state scoping rule, and the
`graphicsLayer` phase-deferral rule for custom animation modifiers, under **Compose Conventions** /
**Compose Performance**.

**Open items:**
- No Macrobenchmark numbers captured (no physical device this session) — the `:baselineprofile`
  module already exists; a real cold-startup + scroll `FrameTimingMetric` P50/P90/P99 pass on
  release + R8 would confirm these fixes actually move frame times.
- Compose Compiler stability reports and a Layout Inspector recomposition-count pass not done this
  session.
- `StatisticsViewModel` has leftover `Timber.tag("StatisticsDebug")` debug logging — cleanup
  candidate, not a perf issue.
