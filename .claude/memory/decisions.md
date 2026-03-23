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
