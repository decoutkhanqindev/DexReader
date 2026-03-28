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

## 2026-03-27 (session 4)

### One-time event pattern chosen for auth navigation
`onLoginSuccess`/`onRegisterSuccess`/`onSubmitSuccess` callbacks in Content (UI) layer
violate Route/Screen separation — UI should not decide where to navigate.
**Decision:** Add `Channel<AuthEvent>` to each auth ViewModel. `dismissDialog()` emits
`AuthEvent.NavigateOnSuccess` when dismissing a success state. Screen/Route layer collects
via `LaunchedEffect(Unit)`. Success callbacks removed from all 3 Content files.
**Rejected:** Keeping callbacks in Content (HIGH severity architectural violation).
**Rejected:** SharedFlow — `Channel.BUFFERED` is sufficient for one-shot nav events.

### @Preview functions removed from auth Content files
User requested removal. Deleted 4 previews per file; removed `Preview`, `MaterialTheme`,
`DexReaderTheme` imports that were preview-only.

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

---

## 2026-03-27 (session 5)

### Composable parameter ordering — full auth/ sweep complete (definitions + call sites)
All 13 auth composable definitions reordered to: required params → optional params → modifier → required lambdas → optional lambdas → content.
All inter-auth call sites also reordered to match definition order (named args everywhere, no breakage).
NavGraph call sites for LoginScreen/RegisterScreen/ForgotPasswordScreen also reordered.
**Agents used:** 5 parallel worktree agents — applied changes to main tree directly (worktrees auto-cleaned). Agents 2+3 also ran `/simplify` successfully.

### Batch agents write to main tree, not isolated worktrees
Observation: despite `isolation: "worktree"`, agents applied file edits to the main working tree.
Worktrees appear for in-progress agents, then auto-clean after agent exits (regardless of commit status).
**How to apply:** Do not rely on worktree isolation for file change isolation between parallel agents in this project.

### `viewModel` param stays last in Screen composables (DI convention)
LoginScreen, RegisterScreen, ForgotPasswordScreen all have `viewModel: VM = hiltViewModel()` as last param.
**Decision:** Keep last — this is the standard Hilt/Compose convention (rarely passed by callers, primarily for testing).
**Rejected:** Moving `viewModel` to group 2 (optional non-lambda before modifier) — too unconventional.

### Call site arg order must mirror definition param order
User explicitly requested call sites reordered to match definition order.
This applies to ALL composables used in auth/ — including shared ones (NotificationDialog, SubmitButton, ActionButton, Text, Icon).
**Pending:** NotificationDialog/SubmitButton/ActionButton definitions still have violations; their call sites in auth/ also need updating.

---

## 2026-03-28

### `viewModel` param position — REVERSAL of prior session 5 decision
Session 5 recorded: "keep `viewModel` last — standard Hilt/Compose DI convention."
**Reversed:** `viewModel: VM = hiltViewModel()` is an optional param (has a default value).
Per convention, optional params belong in group 2 (before `modifier`), not last.
**Decision:** Move `viewModel` to FIRST position (before `modifier`) in all 3 Screen composables.
Applied to: `LoginScreen`, `RegisterScreen`, `ForgotPasswordScreen`.
**Why it's safe:** NavGraph call sites use named parameters — reordering doesn't break callers.
**Rejected:** "Keep last as DI convention" — the convention rule is stricter than the DI idiom here.

### Material composable call-site audit — `OutlinedTextField` modifier position
Audited all 23 Material composable call sites (Text, Icon, Spacer, IconButton, OutlinedTextField) in auth/.
20/23 were already correct. Only violation: `modifier` last in 3 `OutlinedTextField` calls.
**Decision:** Move `modifier` to 3rd position (after `value`, `onValueChange`) in all 3 input field files.
**Why:** Canonical `OutlinedTextField` signature order; naming arg position is enforced by convention not compiler.
All Text/Icon/Spacer/IconButton calls were already in correct canonical order — no changes needed there.

### One-time event refactor dropped (user decision)
`AuthEvent` / `Channel<AuthEvent>` pattern for auth navigation was previously planned.
**Decision:** Dropped entirely by user request. Do not resume this work.
Remaining effort will focus exclusively on composable parameter reordering.

---

## 2026-03-28 (session 2)

### Trailing lambda rule — clarified (supersedes earlier understanding)
Four distinct cases:
1. `content: @Composable () -> Unit` → always trailing `{ }`
2. `<= 1 lambda total` → trailing `{ }`
3. `> 1 lambda, same type` (e.g. all `() -> Unit`) → all named inside parens
4. `action: () -> Unit` + `content: @Composable () -> Unit` → `action = { }) { content }` (action named, content trailing)
**Consequence:** `Button(onClick = ...) { content }`, `IconButton(onClick = ...) { Icon(...) }`, `FloatingActionButton(onClick = ...) { content }` are all CORRECT. No change needed.

### Text arg ordering — `style` always last
Canonical `Text` signature has `style: TextStyle` as the last (17th) parameter.
**Decision:** In all Text call sites, `style` must be the final named arg.
Also: `fontStyle(5)` before `fontWeight(6)`; `overflow(12)` before `maxLines(14)`.
Applied to 14 files in `common/` via commit `b048a7e`.

### Material composable canonical arg order (established)
- `Button/TextButton/IconButton/FAB`: `onClick, modifier, enabled/colors, shape/containerColor, ...`
- `CenterAlignedTopAppBar`: `title, modifier, navigationIcon, actions, ..., colors`
- `AlertDialog`: `onDismissRequest, confirmButton, modifier, dismissButton, icon, title, text, shape`
- `NavigationDrawerItem`: `label, selected, onClick, modifier, icon, badge, shape`
- `Row`: `modifier, horizontalArrangement, verticalAlignment`
- `LazyVerticalGrid`: `columns` (required) first, then `modifier, state, ...`

### Explore agents are unreliable for code audit
Agents hallucinated "OK" for files that actually had violations (e.g. said ActionButton used `content = {}` named, but file had trailing lambda).
**Decision:** Always verify with direct `Read` tool reads for files that will be edited. Grep is reliable for pattern searches.

---

## 2026-03-29

### Trailing lambda rule — `≤1 lambda` applies even to non-content action lambdas
User clarified: `<= 1 lambda total → trailing {}` applies to ANY single lambda, not just `@Composable` content.
E.g. `CategoryTypeHeader(type, isExpanded, modifier) { onExpandClick() }` ✅
**Why:** Kotlin convention; trailing lambda is idiomatic for any single final lambda param.

### Linter auto-renames params after save
The project linter normalises param names on save: `mangaList→items`, `onSelectedManga/onCategoryClick→onItemClick`, `categoryList→items`.
**How to apply:** Always `Read` a file fresh before editing if any previous save may have triggered the linter.

### Card canonical arg order
`Card(onClick, modifier, shape, ...)` — `modifier` before `shape`. Same pattern as Button.
**Why:** Material3 Card signature mirrors Button ordering.
