# Changelog

Dated log of notable multi-file / cross-cutting work sessions. Newest entry first.

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
