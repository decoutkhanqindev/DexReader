# Session Progress

## Completed ✓
- [x] Compose performance fixes, Timber, dismissError/dismissSuccess, AuthDialogState
- [x] `PasswordInputField`: `isConfirmed` → `label: String`
- [x] @Preview removed from all 3 Content files
- [x] Screen composables — `viewModel` moved to 1st position
- [x] All auth/ composable definitions + call sites: correct param order
- [x] All shared composable definitions: correct order
- [x] All common/ composable definitions (31 files): correct order
- [x] All Material/Compose call sites in `common/` (14 files) — commit b048a7e
- [x] `common/texts/LoadPageErrorMessage.kt` — missed violations fixed
- [x] `presentation/screens/home/` — full audit + fix
- [x] `presentation/screens/categories/` — full audit + fix
- [x] `presentation/screens/manga_details/` — full audit + fix (10 violations, 7 files)
- [x] `presentation/screens/reader/` — full audit + fix (7 files incl. NavGraph)
- [x] `presentation/screens/profile/` — full audit + fix (5 files + NavGraph)
- [x] `presentation/screens/settings/` — full audit + fix (4 files + NavGraph)
- [x] `presentation/screens/search/` — full audit + fix (8 files + NavGraph)

## Remaining
**None. All presentation/screens/ directories complete.**

**Next step: run build to verify no compile errors across all changed files.**

## Rules Applied
1. Definition param order: required non-lambda → optional non-modifier → modifier → required lambdas → optional lambdas
2. `viewModel` ALWAYS FIRST in Screen composables
3. Call site named arg order mirrors definition order
4. Trailing lambda: ≤1 lambda total → trailing `{ }`; >1 same-type → all named; action+content → action named, content trailing
5. Text: `style` always last; `fontStyle(5)` before `fontWeight(6)`; `overflow(12)` before `maxLines(14)`
6. Row: `modifier, horizontalArrangement, verticalAlignment`
7. Box: `modifier, contentAlignment, ...`
8. Column: `modifier, verticalArrangement, horizontalAlignment`
9. Button/FAB/IconButton/TextButton: `onClick, modifier, ...`
10. Card (clickable): `onClick, modifier, shape, ...`
11. Card (non-clickable): `modifier, shape, colors, elevation, ...`
12. CenterAlignedTopAppBar: `title, modifier, navigationIcon, actions, ..., colors`
13. TextField: `value, onValueChange, modifier, ...`
14. DropdownMenuItem: `text, onClick, modifier, ..., leadingIcon, ...`
15. ActionButton: `isEnabled, modifier, onClick, content` — modifier IS before onClick
