# Current Task

## Task
Data layer strict Kotlin double-check review — all fixes applied. Awaiting commit approval + ExceptionMapper simplification decision.

## Files Modified (staged, not committed)
- `data/mapper/ExceptionMapper.kt` — toAuthException() + toAuthFlowException() added; grouped by context; CancellationException guard removed from toAuthFlowException()
- `data/mapper/ApiParamMapper.kt` — runCatching replaced with entries.find; String? → String with sensible defaults
- `data/network/firebase/auth/FirebaseAuthSource.kt` — deleteCurrentUser() interface method added
- `data/network/firebase/auth/FirebaseAuthSourceImpl.kt` — deleteCurrentUser() implemented
- `data/repository/user/UserRepositoryImpl.kt` — toAuthException(), toAuthFlowException(), deleteCurrentUser() rollback, toFirestoreException()
- `data/local/database/StringListTypeConverter.kt` — non-null, emptyList() fallback, @JvmStatic
- `data/network/firebase/dto/request/FavoriteMangaRequest.kt` — @Exclude on id field
- `data/network/firebase/dto/request/ReadingHistoryRequest.kt` — @Exclude on id field
- `data/network/firebase/dto/response/FavoriteMangaResponse.kt` — @Exclude on id field
- `data/network/firebase/dto/response/ReadingHistoryResponse.kt` — @Exclude on id field

## Last Action
Context window compressed mid-session. User invoked /save. All staged changes verified BUILD SUCCESSFUL. User still deciding on ExceptionMapper simplification.

## Context to Resume
- `git add` was run for all 10 files; `git commit` has NOT been run
- Build verified: `./gradlew assembleDebug` → BUILD SUCCESSFUL
- ExceptionMapper has 6 functions grouped: Retrofit / Cache / Firestore / Auth
- User is deciding: keep toCacheException() and toAuthFlowException() or drop them (both are thin wrappers around Unexpected)
- toAuthException() should STAY — it has real branching logic (3 Firebase Auth types → 3 BusinessException.Auth subtypes)
