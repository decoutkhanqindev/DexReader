# Blockers

No blockers.

---

## Previously resolved blockers (archived)

### RESOLVED (2026-03-20, session 3) — 5 mapper audit issues
All 5 correctness/design issues from strict-kotlin-reviewer audit implemented. Build passes.

### RESOLVED (2026-03-20, session 2) — `MenuDrawer` / `MenuItemValue` compile errors
`MenuItemRow.kt` `titleRes` → `nameRes` fix and `MenuDrawer.kt` `String → MenuItemValue` bridge
were resolved via the type-safe navigation migration commit (`267105d`).

### RESOLVED (2026-03-20) — `presentation/value/` + `*Value` migration
`MangaModel.kt` referenced non-existent `*Value` types. Resolved — all 7 `*Value` files exist.
