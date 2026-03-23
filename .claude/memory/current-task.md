# Current Task

## Task
Implement all review fixes from the strict Kotlin review of domain/ + data/ layers.
Plan file: `C:\Users\ADMIN\.claude\plans\ancient-wobbling-russell.md`

## Files Staged (not yet committed — from prior session + this session)
- `data/mapper/ExceptionMapper.kt` — toUnexpectedException() replaces toCacheException/toAuthFlowException; blank line fixes
- `data/repository/manga/CacheRepositoryImpl.kt` — toCacheException → toUnexpectedException
- `data/repository/user/UserRepositoryImpl.kt` — toAuthFlowException → toUnexpectedException; linter reordered observeCurrentUser's .catch before .flowOn
- All other prior-session staged files (StringListTypeConverter, Firebase DTOs, FirebaseAuthSource deleteCurrentUser, ApiParamMapper)

## Last Action
Plan written to `ancient-wobbling-russell.md`. ExitPlanMode rejected by user (hit context limit). Session saved before implementation began.

## Context to Resume
- Git commit 0 (staged files above) has NOT been run yet
- Implementation of Commits 1–7 has NOT started
- The plan is complete and approved ("your plan look good")
- Start with: commit staged files, then Commit 1 (exception safety)
