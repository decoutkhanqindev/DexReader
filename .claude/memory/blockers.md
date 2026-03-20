# Blockers

No blockers.

Domain layer and data layer audits are fully complete and committed. No known open issues.

---

## Previously resolved blockers (archived)

### RESOLVED (2026-03-20, session 6) — Latent crash in `ApiParamMapper` for `UNKNOWN` enum values
`MangaStatus.toApiParam()` and `MangaContentRating.toApiParam()` used `valueOf(name)` which threw
`IllegalArgumentException` for `UNKNOWN`. Fixed by switching to `entries.find` with nullable return.
`FavoriteMangaMapper` and `CategoryRepositoryImpl` updated. Build passes.

### RESOLVED (2026-03-20, session 4) — `CacheRepository.addChapterCache` mangaId leaking domain interface
Resolved by adding `val mangaId: String` to `ChapterPages`. Full chain updated. Build passes.

### RESOLVED (2026-03-20, session 3) — 5 mapper audit issues
All 5 correctness/design issues from strict-kotlin-reviewer audit implemented. Build passes.

### RESOLVED (2026-03-20, session 2) — `MenuDrawer` / `MenuItemValue` compile errors
Resolved via the type-safe navigation migration commit (`267105d`).

### RESOLVED (2026-03-20) — `presentation/value/` + `*Value` migration
All 7 `*Value` files exist. Resolved.
