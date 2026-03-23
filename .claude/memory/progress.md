# Session Progress

## Data Layer Double-Check Review (2026-03-23)

### Completed This Session

#### Agent reviews (7 parallel workers)
- [x] Unit 1 — Mappers: ApiParamMapper entries.find, ExceptionMapper grouping + toAuthException/toAuthFlowException
- [x] Unit 2 — Network API layer: IsoDateTimeAdapter already fixed in main; no new changes
- [x] Unit 3 — Firebase layer: @Exclude DTO fix, toAuthException/toAuthFlowException added, UserRepositoryImpl refactored
- [x] Unit 4 — Local data layer: StringListTypeConverter nullable → non-null, emptyList() fallback, @JvmStatic
- [x] Unit 5 — Manga repos: confirmed clean, flagged MOST_VIEWED→createdAt as product question
- [x] Unit 6 — User/Settings repos: updateUserProfile onCatch → toFirestoreException(); deleteCurrentUser() rollback
- [x] Unit 7 — DI + Exception hierarchy: LOST (worktree gone, never committed) — exception hierarchy already fixed in main from prior session

#### Applied directly to main (staged, not committed)
- [x] ExceptionMapper.kt: toAuthException() + toAuthFlowException() added; grouped Retrofit/Cache/Firestore/Auth; CancellationException guard removed
- [x] ApiParamMapper.kt: runCatching replaced with entries.find; String? → String with sensible defaults (ON_GOING, SAFE, DESC, ENGLISH)
- [x] FirebaseAuthSource.kt + Impl: deleteCurrentUser() added
- [x] UserRepositoryImpl.kt: toAuthException(), toAuthFlowException(), deleteCurrentUser() rollback, toFirestoreException()
- [x] StringListTypeConverter.kt: non-null, emptyList() fallback, @JvmStatic
- [x] Firebase DTOs (4 files): @Exclude on id fields (was wrongly @PropertyName)
- [x] Build verified: BUILD SUCCESSFUL

### Still To Do

- [ ] **Commit all staged changes to main** (user reviewing ExceptionMapper first)
- [ ] Resolve ExceptionMapper simplification: keep or drop toCacheException/toAuthFlowException?
- [ ] Fix CategoryRepositoryImpl: mapNotNull → map (since toApiParam() no longer returns null)
- [ ] Domain layer fixes (separate): UpdateUserProfileUseCase avatar bug, ClearExpiredCacheUseCase.clock internal var

### Single Most Important Next Step
**Get user decision on ExceptionMapper simplification, then commit all staged changes.**
