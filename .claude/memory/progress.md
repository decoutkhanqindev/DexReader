# Session Progress

## Review Fix Implementation (2026-03-23 — plan approved, not yet started)

### Pre-work
- [x] Strict Kotlin review completed (domain/ + data/ layers)
- [x] Plan written: `C:\Users\ADMIN\.claude\plans\ancient-wobbling-russell.md`
- [ ] **Commit 0 — commit all currently staged files (prior session + ExceptionMapper simplification)**

### Commit 1 — Exception Safety
- [ ] `ExceptionMapper.kt`: CancellationException guard in `toUnexpectedException()`
- [ ] `ExceptionMapper.kt`: CancellationException guard + UNAVAILABLE/DEADLINE_EXCEEDED in `toFirestoreFlowException()`
- [ ] `ExceptionMapper.kt`: HTTP status branching in `toDomainException()` (4xx→Unexpected, 5xx→ServerUnavailable)
- [ ] `UserRepositoryImpl.kt`: fix CE swallow in rollback inner try/catch (deleteCurrentUser)
- [ ] `UserRepositoryImpl.kt`: wrap `logout()` in `runSuspendCatching`

### Commit 2 — Flow Ordering
- [ ] `UserRepositoryImpl.kt`: `observeUserProfile()` — catch before flowOn
- [ ] `FavoritesRepositoryImpl.kt`: `observeFavorites()` — catch before flowOn
- [ ] `FavoritesRepositoryImpl.kt`: `observeIsFavorite()` — catch before flowOn
- [ ] `HistoryRepositoryImpl.kt`: `observeHistory()` — catch before flowOn

### Commit 3 — API Param UNKNOWN
- [ ] `CategoryRepositoryImpl.kt`: filter UNKNOWN from statusFilter + contentRatingFilter before map

### Commit 4 — Settings Persistence
- [ ] `SettingsRepositoryImpl.kt`: intPreferencesKey+ordinal → stringPreferencesKey+name

### Commit 5 — FirebaseAuthSource Interface
- [ ] `FirebaseAuthSource.kt`: FirebaseUser? → User?, Flow<FirebaseUser?> → Flow<User?>
- [ ] `FirebaseAuthSourceImpl.kt`: move .toUser() calls inside register() and observeCurrentUser()
- [ ] `UserRepositoryImpl.kt`: remove .toUser() calls, remove .map { it?.toUser() }

### Commit 6 — Minor Fixes
- [ ] `ClearExpiredCacheUseCase.kt`: var→val clock, 24L*60*60*1000 Long fix
- [ ] `FavoriteMangaMapper.kt`: remove dead ?: "" on status.toApiParam()
- [ ] `FavoriteManga.kt`: DEFAULT_ADDED_AT Long? = null → meaningful constant
- [ ] `IsoDateTimeAdapter.kt`: formatter.get()!! → requireNotNull
- [ ] `ChapterCacheDatabase.kt` + `LocalDataModule.kt`: add explanatory comments

### Commit 7 — callbackFlow Refactor
- [ ] `FirebaseFirestoreSourceImpl.kt`: extract await() before callbackFlow in observeFavorites() + observeHistory()

### **Single Most Important Next Step**
**Commit 0: `git add` the staged files and commit, then start Commit 1.**
