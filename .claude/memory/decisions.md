## Archive (2026-03-18 through 2026-03-23)

Key outcomes from earlier sessions:
- Presentation layer domain isolation complete: domain types removed from composables/UiState; ViewModels are the translation boundary. `*UiModel`/`*UiError` naming.
- `CategoryGroup` sealed → `CategoryType` enum; domain model properties renamed to domain-meaningful names.
- All 8 mappers wrapped in `object`; domain models have `companion object` default constants.
- `Chapter.NavPosition` + `determineNavPosition()` extracted to domain layer.
- `CancellationException` always rethrown — fixed in FavoritesViewModel + HistoryViewModel.
- Exception hierarchy: `data class` → `class` for all exceptions (wrong equals/hashCode/copy semantics).
- `UpdateUserProfileUseCase` avatar bug: `avatarUrl = newAvatarUrl` silently wiped avatar. Fixed to `newAvatarUrl ?: currentUser.avatarUrl`.
- `IsoDateTimeAdapter`: `SimpleDateFormat` → `ThreadLocal`; `fromJson` delegates to `TimeAgo.parseIso8601ToEpoch()`.
- `FirebaseFirestoreSource` monolith split into 3 focused interfaces (user/favorite/history) — ISP.
- `ApiParamMapper`: `runCatching { valueOf() }` → `entries.find`; non-null return with sensible defaults.
- `ExceptionMapper`: `toCacheException` + `toAuthFlowException` merged → `toUnexpectedException`; `toAuthException()` kept (real branching). `toUnexpectedException` + `toFirestoreFlowException` have explicit `CancellationException` guard (was "not needed" — reversed; `toUnexpectedException` is public).
- Firebase DTO id fields: `@PropertyName` → `@Exclude` (id is doc ID, not a stored field).
- Registration rollback: `logout()` → `deleteCurrentUser()` (orphaned auth account blocked re-registration).
- `callbackFlow + await` fix: wrapped in `flow { emitAll(callbackFlow { }) }` — `await()` unsafe inside `callbackFlow {}` before `awaitClose`.
- `ThemeMode` persistence: ordinal → name (ordinal silently wrong if enum order changes).
- `FirebaseAuthSource`: `FirebaseUser` return type → `User`/`Flow<User?>` (leaked SDK type removed).
- `NetworkDataModule` split → `di/network/ApiModule.kt` + `di/network/FirebaseModule.kt`.
- All 4 DI qualifiers removed; replaced with `BuildConfig` constants + companion constants.

---

## 2026-03-27

### Timber added as project dependency
Added `timber = "5.0.1"` to `libs.versions.toml`, `implementation(libs.timber)` to `build.gradle.kts`.
`DexReaderApplication` plants `DebugTree` in `onCreate()` (DEBUG only).
All 3 auth ViewModels use `Timber.tag(TAG).d(...)`. A linter hook enforces this on save.

### `LoginUiState.userError` removed
Field was never read by any composable. `UserError.NotFound` now maps to `isError = true`.
**Why:** Dead state fields silently diverge from UI. All unrecoverable errors route through the single `isError` dialog path.

### `update*` functions — reset scope narrowed
Each `update*` previously reset `isLoading`/`isSuccess`/`isError` in addition to clearing its own error field.
**Decision:** Only clear the directly related error field.
**Why:** Resetting `isLoading`/`isSuccess` on a keystroke dismisses mid-flight UI state (e.g. hides loading spinner if user types while request is in-flight).

### Dialog visibility — `rememberSaveable` flags removed
`isShowErrorDialog`/`isShowSuccessDialog` in `*Content` created split truth between local state and VM.
**Decision:** `dismissError()`/`dismissSuccess()` in each VM; dialogs driven purely from UiState; `*Content` gets `onDismissError`/`onDismissSuccess` wired from `*Screen`.
**Rejected:** `SharedFlow<Event>` — heavier pattern, unnecessary.

### `OutlinedTextFieldDefaults.colors()` cannot use `remember {}`
`@Composable` function cannot be called inside `remember {}`'s `@DisallowComposableCalls` lambda.
**Decision:** Single `val colorScheme = MaterialTheme.colorScheme` local; `colors()` called inline. Comment added to all 3 input field files.
