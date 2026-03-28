# Session Progress

## Completed ✓
- [x] Compose performance fixes, Timber, dismissError/dismissSuccess, AuthDialogState
- [x] `PasswordInputField`: `isConfirmed` → `label: String`
- [x] @Preview removed from all 3 Content files
- [x] Screen composables — `viewModel` moved to 1st position (before modifier)
- [x] `EmailInputField`, `NameInputField`, `PasswordInputField` — `modifier` moved to 3rd in `OutlinedTextField`
- [x] All auth/ custom composable definitions: correct param order
- [x] All 23 Material composable call sites in auth/: correct order
- [x] All shared composable definitions (`NotificationDialog`, `SubmitButton`, `ActionButton`): correct order
- [x] All 31 custom composable definitions in `common/`: correct order (verified, no changes needed)
- [x] All Material/Compose call sites in `common/` (14 files): canonical arg order + Text style last — commit b048a7e

## Remaining — Feature Screens

**Next step: audit + fix `presentation/screens/home/` call sites**

Then continue with:
- [ ] `presentation/screens/categories/`
- [ ] `presentation/screens/manga_details/`
- [ ] `presentation/screens/reader/`
- [ ] `presentation/screens/profile/`
- [ ] `presentation/screens/settings/`
- [ ] `presentation/screens/search/`

## Rules to Apply
1. Custom composable definition param order: required → optional (non-modifier) → modifier → required lambdas → optional lambdas → content
2. Call site named arg order mirrors definition order
3. Trailing lambda: `action + content` → `action = { }) { content }` | `> 1 same-type lambdas` → all named
4. Text: `style` always last
5. Row: `horizontalArrangement` before `verticalAlignment`
6. Button/FAB/IconButton/TextButton: `onClick, modifier, ...`
7. CenterAlignedTopAppBar: `title, modifier, navigationIcon, ...`
