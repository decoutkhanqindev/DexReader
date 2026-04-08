## Archive (2026-03-18 through 2026-03-27)

Key outcomes from earlier sessions:
- Presentation layer domain isolation complete: domain types removed from composables/UiState; ViewModels are the translation boundary. `*UiModel`/`*UiError` naming.
- `CategoryGroup` sealed → `CategoryType` enum; domain model properties renamed to domain-meaningful names.
- All 8 mappers wrapped in `object`; domain models have `companion object` default constants.
- `Chapter.NavPosition` + `determineNavPosition()` extracted to domain layer.
- `CancellationException` always rethrown — fixed in FavoritesViewModel + HistoryViewModel.
- Firebase DTO id fields: `@PropertyName` → `@Exclude`; Registration rollback: `logout()` → `deleteCurrentUser()`.
- `ThemeMode` persistence: ordinal → name; `FirebaseAuthSource`: `FirebaseUser` → `User`/`Flow<User?>`.
- `NetworkDataModule` split → `ApiModule.kt` + `FirebaseModule.kt`; all 4 DI qualifiers removed.
- Timber added; `LoginUiState.userError` removed; `update*` reset scope narrowed to own field only.
- Dialog visibility: `rememberSaveable` flags removed; dialogs driven from UiState via `dismissError()`/`dismissSuccess()`.
- `OutlinedTextFieldDefaults.colors()` cannot use `remember {}` — call inline instead.
- One-time event refactor (`AuthEvent`) dropped by user — do not resume.

---

## 2026-03-27 (session 5)

### Composable parameter ordering — full auth/ sweep complete
All 13 auth composable definitions reordered: required params → optional params → modifier → required lambdas → optional lambdas.
All call sites (inter-auth + NavGraph) also reordered with named args.

### Batch agents write to main tree, not isolated worktrees
Despite `isolation: "worktree"`, agents apply edits to main tree. Worktrees auto-clean after exit.
**How to apply:** Do not rely on worktree isolation for file change isolation between parallel agents.

### Call site arg order must mirror definition param order
User explicitly requested call sites reordered. Applies to ALL composables including shared ones.

---

## 2026-03-28

### `viewModel` param position — FIRST in Screen composables (reversal)
Session 5 recorded "keep last." Reversed: `viewModel` is optional (has default) — optional params belong before `modifier`.
**Decision:** `viewModel: VM = hiltViewModel()` FIRST in all Screen composables.
**Why it's safe:** NavGraph call sites use named parameters.

### Material composable call-site audit — `OutlinedTextField` modifier position
`modifier` → 3rd position (after `value`, `onValueChange`) in all 3 input field files.

### One-time event refactor dropped (user decision)
`AuthEvent` / `Channel<AuthEvent>` dropped entirely. Do not resume.

---

## 2026-03-28 (session 2)

### Trailing lambda rule — clarified (supersedes earlier understanding)
1. `content: @Composable () -> Unit` → always trailing `{ }`
2. `<= 1 lambda total` → trailing `{ }`
3. `> 1 lambda, same type` → all named inside parens
4. `action: () -> Unit` + `content: @Composable () -> Unit` → action named, content trailing
`Button/IconButton/FAB(onClick = ...) { content }` — all CORRECT.

### Text arg ordering — `style` always last; `fontStyle(5)` before `fontWeight(6)`
### Material composable canonical arg order (established)
- `Button/TextButton/IconButton/FAB`: `onClick, modifier, enabled/colors, ...`
- `CenterAlignedTopAppBar`: `title, modifier, navigationIcon, actions, ..., colors`
- `AlertDialog`: `onDismissRequest, confirmButton, modifier, dismissButton, icon, title, text, shape`
- `Row`: `modifier, horizontalArrangement, verticalAlignment`
- `LazyVerticalGrid`: `columns` first, then `modifier, state, ...`

### Explore agents unreliable for code audit
Agents hallucinate "OK" for files with violations. Always verify with direct `Read`/`Grep`.

---

## 2026-03-29

### Trailing lambda rule — `≤1 lambda` applies to ANY single lambda (not just @Composable content)
`CategoryTypeHeader(type, isExpanded, modifier) { onExpandClick() }` ✅

### Linter auto-renames params after save
Normalises: `mangaList→items`, `onSelectedManga/onCategoryClick→onItemClick`, `categoryList→items`.
**How to apply:** Always `Read` file fresh before editing after any save.

### Card canonical arg order
`Card(onClick, modifier, shape, ...)` — mirrors Button ordering.

---

## 2026-04-08

### ModalBottomSheet canonical arg order
`ModalBottomSheet(onDismissRequest, modifier, sheetState, ...)` — `onDismissRequest` first (required lambda).
**Applied to:** `ChapterLanguageListBottomSheet.kt`

### LoadPageErrorMessage: single lambda → trailing at all call sites
**Applied to:** `MangaChaptersSection`, `MangaChapterList`

---

## 2026-04-08 (reader/ session)

### BottomAppBar: `modifier` first, `actions` trailing @Composable
`BottomAppBar(modifier, containerColor, contentColor, …) { actions }`
**Applied to:** `NavigateChapterBottomBar.kt`

### HorizontalPager: `state` required first
### ZoomableAsyncImage (telephoto): `model, contentDescription, modifier, state, contentScale, …`
### DetailsTopBar call sites: required lambdas after modifier
`DetailsTopBar(title, isSearchEnabled, modifier, onNavigateBack, onNavigateToSearchScreen)`

---

## 2026-04-08 (profile/ session)

### ActionButton canonical: `isEnabled, modifier, onClick, content`
`modifier` IS before `onClick` in the definition (confirmed by direct read).
Plan had a wrong "Violation C" for ActionButton call sites — they were already correct.
**How to apply:** Do not reorder `ActionButton(modifier = ..., onClick = { ... })` call sites.

### profile/ sweep complete (5 files + NavGraph)
Definitions fixed: `ProfileNameEdit`, `ProfilePicturePicker`, `UpdateAndLogoutUserBottomBar`, `ProfileContent`, `ProfileScreen`.
`ProfileScreen` — `viewModel` first, `modifier` before lambdas.
`ProfileContent` — `ProfilePicturePicker` and `ProfileNameEdit` call sites both converted to trailing lambda.

---

## 2026-04-08 (settings/ session)

### Non-clickable Card: `modifier` is first param (not `shape`)
Clickable `Card(onClick, modifier, shape, ...)` vs non-clickable `Card(modifier, shape, colors, elevation, ...)`.
`ThemeOptionList` had `Card(shape = ..., elevation = ..., modifier = ...)` — fixed to `modifier` first.
**How to apply:** Check if Card has `onClick` before deciding which canonical order applies.

### Box canonical: `modifier, contentAlignment, propagateMinConstraints`
`SettingsContent` had `Box(contentAlignment = ..., modifier = ...)` — fixed.

### Column canonical: `modifier, verticalArrangement, horizontalAlignment`
`SettingsContent` had `Column(verticalArrangement = ..., horizontalAlignment = ..., modifier = ...)` — fixed.

### settings/ sweep complete (4 files + NavGraph)
`ThemeOptionItem`, `ThemeOptionList`, `SettingsContent`, `SettingsScreen` definitions all fixed.
`ThemeOptionItem` and `ThemeOptionList` each had 1 lambda → trailing at call sites.
`SettingsContent` `NotificationDialog` ×3 all had `onConfirmClick` first — fixed to `title` first.

---

## 2026-04-08 (search/ session — final)

### search/ sweep complete (8 files + NavGraph)
All composable definitions and call sites in `presentation/screens/search/` fixed.
- `SuggestionItem`: `DropdownMenuItem` modifier 3rd; `Text` overflow before maxLines, style last
- `SuggestionList`, `SuggestionsSection`, `ResultsSection`: definitions + call sites fixed
- `SearchBar`: `CenterAlignedTopAppBar` modifier 2nd; `TextField` modifier 3rd
- `SearchContent`, `SearchScreen`: definitions + all inner call sites fixed
- `NavGraph` SearchScreen call: modifier before lambdas

### Composable param ordering refactor — COMPLETE
All `presentation/screens/` directories done: auth/ → common/ → home/ → categories/ → manga_details/ → reader/ → profile/ → settings/ → search/.
