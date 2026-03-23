# Blockers

## Open Decision: ExceptionMapper simplification

User deciding whether to drop thin wrapper functions:

- `toCacheException()` — only: `is DomainException -> throw this; else -> Unexpected`. No unique mapping logic.
- `toAuthFlowException()` — same structure. Only one call site: `UserRepositoryImpl.observeCurrentUser()`.
- `toAuthException()` — KEEP. Real branching: 3 Firebase Auth types → 3 BusinessException.Auth subtypes.

**Blocking:** git commit of all 10 staged files.

## Pending Cleanup (not blocking commit)

- `CategoryRepositoryImpl`: `mapNotNull { it.toApiParam() }` → `map { it.toApiParam() }` (toApiParam() is now non-null)
- Domain layer: UpdateUserProfileUseCase avatar bug, ClearExpiredCacheUseCase.clock internal var (deferred)

---

## Previously Resolved

### RESOLVED (2026-03-23) — Data layer double-check review round 2
All 10 files staged, build verified. Awaiting ExceptionMapper decision + commit.

### RESOLVED (2026-03-22) — 3 worktrees awaiting commit
Worktrees were lost (never committed). Exception hierarchy fixes already in main from prior commits.

### RESOLVED (2026-03-22) — Data layer double-check review
All 6 review units completed. Key fixes: toFirestoreFlowException(), ChapterPagesMapper, SimpleDateFormat. All committed.
