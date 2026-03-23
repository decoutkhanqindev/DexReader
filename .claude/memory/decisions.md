## 2026-03-23 (session 2)

### Decision: Split FirebaseFirestoreSource into 3 focused sources

**What was decided:**
Replaced the single `FirebaseFirestoreSource` interface + `FirebaseFirestoreSourceImpl` with three pairs:
- `firestore/user/` — `FirebaseUserFirestoreSource` + Impl
- `firestore/favorite/` — `FirebaseFavoriteFirestoreSource` + Impl
- `firestore/history/` — `FirebaseHistoryFirestoreSource` + Impl

Each impl has its own `@Inject constructor(FirebaseFirestore)` and `usersCollectionRef`. Each repository now injects only the one interface it needs. `NetworkDataModule` has 3 `@Provides` bindings.

**Reasoning:** The monolithic interface violated ISP — each repository only used a third of it. Splitting makes injection precise, reduces coupling, and mirrors the existing per-concern repository structure.

**Alternatives rejected:**
- Keep monolithic but split only the impl — rejected; the interface boundary still forces unnecessary awareness of unrelated methods.

---

## Archive (2026-03-18 through 2026-03-22)

Consolidated decisions from earlier sessions. Key outcomes:
- Presentation layer domain isolation complete (all 8 batches): domain types removed from all composables/UiState; ViewModels are the translation boundary. `*UiModel` naming for data/enum models, `*UiError` for error sealed classes.
- `CategoryGroup` sealed class → `CategoryType` pure enum; `CategoryTypeOption` → `CategoryTypeUiModel`.
- All domain model properties renamed to domain-meaningful names (e.g. `publishAt→publishedAt`, `availableTranslatedLanguages→availableLanguages`).
- All 8 mapper objects wrapped in `object`; all domain models have `companion object` constants for defaults.
- `Chapter.NavPosition` + `determineNavPosition()` extracted to domain layer.
- `CancellationException` always rethrown — fixed in FavoritesViewModel and HistoryViewModel.
- Exception hierarchy: `BusinessException`/`InfrastructureException` subtypes changed from `data class` → `class`; `val rootCause` → `cause` parameter (standard Throwable chaining). Rationale: `data class` generates semantically-wrong `equals`/`hashCode`/`copy` for exceptions.
- `CategoryRepository` default list params extracted to companion constants (allocation fix).
- `ClearExpiredCacheUseCase.clock` moved to constructor parameter (was `internal var` — mutable state on injected singleton).
- `UpdateUserProfileUseCase` avatar bug: `avatarUrl = newAvatarUrl` silently wiped avatar when `null` meant "no change". Fixed to `newAvatarUrl ?: currentUser.avatarUrl`.
- `IsoDateTimeAdapter`: `SimpleDateFormat` property getter (allocates per call) → `ThreadLocal`; `fromJson` delegates to `TimeAgo.parseIso8601ToEpoch()`.

---

## 2026-03-23

### Decision: ApiParamMapper — entries.find instead of runCatching { valueOf() }

**What was decided:**
Forward mappers (`toApiParam()`) replaced `runCatching { ParamEnum.valueOf(name) }.getOrNull()` with `ParamEnum.entries.find { it.name == name }`. Return type changed from `String?` to `String` with sensible defaults (ON_GOING, SAFE, DESC, ENGLISH) for the UNKNOWN sentinel case.

**Reasoning:**
`runCatching` used exceptions for control flow — `valueOf()` throws `IllegalArgumentException` for `UNKNOWN` which has no API counterpart. `entries.find` returns `null` naturally. User preference: "keep it simple, fallback to default like UI does." Non-null return is simpler for call sites and consistent with the UI Option enum pattern.

**Alternatives rejected:**
- `String?` return + `mapNotNull` at call sites — rejected; adds nullable complexity and requires `mapNotNull` in CategoryRepositoryImpl (now redundant).

---

### Decision: ExceptionMapper — toAuthException() + toAuthFlowException() added; grouped by context

**What was decided:**
Two new methods added to centralize Firebase Auth exception mapping (matching the existing Firestore pattern):
- `toAuthException()` — maps FirebaseAuth exceptions to BusinessException.Auth subtypes
- `toAuthFlowException()` — for Flow `.catch {}` on auth streams; rethrows DomainException, wraps else as Unexpected

Functions grouped with section comments: Retrofit | Cache | Firestore (suspend+flow) | Auth (suspend+flow).

`toAuthFlowException()` does NOT need explicit `CancellationException` guard — Flow `.catch {}` never receives `CancellationException` by design (coroutine machinery intercepts it first).

**Still open:** User reviewing whether to simplify further (drop toCacheException, toAuthException, toAuthFlowException).

---

### Decision: Firebase DTO id fields — @PropertyName → @Exclude

**What was decided:**
`FavoriteMangaRequest/Response.id` and `ReadingHistoryRequest/Response.id` were annotated `@PropertyName("manga_id"/"reading_history_id")` — incorrect because `id` is the Firestore document ID, addressed via `.document(id)` by the caller, not stored as a document field. Changed to `@Exclude`.

**Reasoning:**
With `@PropertyName`, Firestore would write `id` as a redundant field in the document body AND try to read it back on deserialization — causing incorrect data or silent overwrites.

---

### Decision: UserRepositoryImpl registration rollback — logout() → deleteCurrentUser()

**What was decided:**
Registration rollback (Firestore write fails after auth account created) changed from `authSource.logout()` to `authSource.deleteCurrentUser()`. New `deleteCurrentUser()` method added to `FirebaseAuthSource` interface and `FirebaseAuthSourceImpl`.

**Reasoning:**
`logout()` only ends the current session — the orphaned Firebase Auth account persists, blocking the user from re-registering with the same email. `deleteCurrentUser()` removes the account entirely so the user can retry.

---

### Decision: toAuthFlowException() — no CancellationException guard needed

**What was decided:**
`toAuthFlowException()` does not rethrow `CancellationException` explicitly. The final form is:
```kotlin
fun Throwable.toAuthFlowException(): Nothing = when (this) {
  is DomainException -> throw this
  else -> throw InfrastructureException.Unexpected(cause = this)
}
```

**Reasoning:**
`Flow.catch {}` never receives `CancellationException` — the coroutine machinery intercepts it before the lambda runs. An explicit guard would be dead code. This matches the existing `toFirestoreFlowException()` pattern (which also has no guard).

**Alternatives rejected:**
- Adding `is CancellationException -> throw this` guard — rejected; dead code that misleads readers into thinking it's necessary.

---

### Decision: ExceptionMapper — toCacheException + toAuthFlowException → toUnexpectedException

**What was decided:**
`toCacheException()` and `toAuthFlowException()` were structurally identical (rethrow DomainException, wrap else as Unexpected) with no unique logic. Merged into a single `Throwable.toUnexpectedException()`. `toAuthException()` kept — it has real branching (3 Firebase Auth types → 3 subtypes).

**Reasoning:** Two functions with identical bodies pretending to be context-specific add fake specificity. One honest function is better.

---

### Decision: Review fix plan — CancellationException guards needed in toUnexpectedException + toFirestoreFlowException

**What was decided (reverses prior "no guard needed" decision for toAuthFlowException):**
- `toUnexpectedException()`: IS a public function, could be called outside Flow.catch — guard is necessary
- `toFirestoreFlowException()`: `else -> throw this` currently rethrows CE by coincidence — guard should be explicit
Plan adds `is CancellationException -> throw this` as first branch in both.

**Reasoning:** The prior session decision was specific to `toAuthFlowException` used only in `Flow.catch`. `toUnexpectedException` is more broadly public. And explicitness over "works by accident."

---

### Decision: callbackFlow + await fix — flow { emitAll(callbackFlow { }) } pattern

**What was decided:**
`observeFavorites()` and `observeHistory()` in `FirebaseFirestoreSourceImpl` call `await()` inside `callbackFlow {}` for cursor-based pagination. Fix: wrap in `flow { ... emitAll(callbackFlow { ... }) }` — the outer `flow {}` is a normal suspend context where `await()` is safe.

**Reasoning:** `callbackFlow {}` is a coroutine scope but designed for callback-to-flow bridging; suspension inside it before `awaitClose` is a structured concurrency violation. Affects only paginated calls (non-null cursor).

---

### Decision: SettingsRepositoryImpl — ordinal → name persistence

**What was decided:**
`ThemeMode` will be persisted by `value.name` (string) instead of `value.ordinal` (int). Key type changes from `intPreferencesKey` to `stringPreferencesKey`.

**Reasoning:** Ordinal silently maps to wrong value if enum order ever changes. Name is stable as long as enum entries aren't renamed (which is a deliberate breaking change, not accidental).

---

### Decision: FirebaseAuthSource interface — remove FirebaseUser

**What was decided:**
`register()` and `observeCurrentUser()` return types changed from `FirebaseUser?`/`Flow<FirebaseUser?>` to `User?`/`Flow<User?>`. Mapping via `UserMapper.toUser()` moves from `UserRepositoryImpl` into `FirebaseAuthSourceImpl`.

**Reasoning:** Leaking a Firebase SDK type through an interface boundary defeats the abstraction. The repository should not need to know about `FirebaseUser`.

---

## 2026-03-23 (session 3)

### Decision: Split NetworkDataModule into ApiModule + FirebaseModule under di/network/

**What was decided:**
`NetworkDataModule.kt` deleted. Replaced by:
- `di/network/ApiModule.kt` — Retrofit/OkHttp providers + qualifier annotations
- `di/network/FirebaseModule.kt` — Firebase providers

**Reasoning:** Two unrelated concerns in one file. `di/network/` subfolder mirrors the existing per-concern structure.

**Alternatives rejected:** Keeping them in `di/` root — user explicitly requested a `network/` folder.

---

### Decision: Remove all 4 DI qualifiers; replace with BuildConfig constants and companion constants

**What was decided:**
All qualifier annotations removed:
- `@BaseUrlQualifier` — `BuildConfig.BASE_URL` inlined into `provideRetrofit()` directly
- `@UploadUrlQualifier` — `BuildConfig.UPLOAD_URL` referenced directly in `MangaRepositoryImpl` + `CategoryRepositoryImpl`
- `@MangaDexApiServiceQualifier` — redundant (only one `Retrofit` binding in the graph)
- `@ThemeModeKeyQualifier` — `"theme_mode"` moved to `companion object { private const val THEME_MODE_KEY }` in `SettingsRepositoryImpl`

**Reasoning:** Qualifiers exist only to disambiguate multiple bindings of the same type. `@MangaDexApiServiceQualifier` was never needed (one Retrofit). The other three disambiguated `String` providers that are better expressed as compile-time constants — no runtime benefit to injecting them through the DI graph.

**Alternatives rejected:** Keeping `@ThemeModeKeyQualifier` for testability — rejected; the key is a fixed string, not a runtime value, so there is nothing to vary in tests.
