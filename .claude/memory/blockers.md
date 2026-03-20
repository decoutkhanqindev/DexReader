# Blockers

No blockers.

The only pending work is committing 4 already-verified staged files (build passes). No unresolved
problems, no open questions, no external dependencies.

---

## Known future issues (not blockers — no immediate action required)

### Latent crash in `ApiParamMapper` for `UNKNOWN` enum values

`ApiParamMapper.toMangaStatus()` and `toMangaContentRating()` call `valueOf(name)` on
`MangaStatusParam` and `MangaContentRatingParam` respectively. Neither of those data-layer param
enums has an `UNKNOWN` entry. If `MangaStatus.UNKNOWN` or `MangaContentRating.UNKNOWN` is ever
passed to these functions, the app will throw `IllegalArgumentException` at runtime.

Concrete scenarios where this could be triggered:
- A `Manga` with `status = MangaStatus.UNKNOWN` (from a malformed API response) is added to
  Favorites — `AddToFavoritesUseCase` passes the manga through, and `FavoriteMangaMapper` or
  `ApiParamMapper` is called with the UNKNOWN status
- A user selects `MangaContentRating.UNKNOWN` as a search filter (unlikely from UI, but possible
  if state is restored incorrectly)

Fix: add guard in `ApiParamMapper` before calling `valueOf`, or add `UNKNOWN` entries to
`MangaStatusParam` and `MangaContentRatingParam` mapped to `null` (to skip them in API calls).

This is a data-layer fix, not domain-layer. Not in scope for current audit pass.

---

## Previously resolved blockers (archived)

### RESOLVED (2026-03-20, session 4) — `CacheRepository.addChapterCache` mangaId leaking domain interface
Resolved by adding `val mangaId: String` to `ChapterPages`. Full chain updated. Build passes.

### RESOLVED (2026-03-20, session 3) — 5 mapper audit issues
All 5 correctness/design issues from strict-kotlin-reviewer audit implemented. Build passes.

### RESOLVED (2026-03-20, session 2) — `MenuDrawer` / `MenuItemValue` compile errors
`MenuItemRow.kt` `titleRes` → `nameRes` fix and `MenuDrawer.kt` `String → MenuItemValue` bridge
were resolved via the type-safe navigation migration commit (`267105d`).

### RESOLVED (2026-03-20) — `presentation/value/` + `*Value` migration
`MangaModel.kt` referenced non-existent `*Value` types. Resolved — all 7 `*Value` files exist.
