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
- [x] `presentation/screens/home/` — full audit + fix (7 violations: HomeScreen def, NavGraph, HomeContent def, MangaListSection def + call sites ×4, HorizontalMangaList call)
- [x] `presentation/screens/categories/` — full audit + fix (CategoryItem, CategoryList, CategoryTypeHeader, CategoryTypeSection, CategoriesContent, CategoriesScreen defs + call sites + NavGraph)

## Remaining — Feature Screens

**Next step: audit + fix `presentation/screens/manga_details/`**

Then continue with:
- [ ] `presentation/screens/reader/`
- [ ] `presentation/screens/profile/`
- [ ] `presentation/screens/settings/`
- [ ] `presentation/screens/search/`

## Rules to Apply
1. Definition param order: required non-lambda → optional non-modifier → modifier → required lambdas → optional lambdas
2. `viewModel` ALWAYS FIRST in Screen composables
3. Call site named arg order mirrors definition order
4. Trailing lambda: ≤1 lambda total → trailing `{ }`; >1 same-type → all named; action+`@Composable` content → action named content trailing
5. Text: `style` always last
6. Row: `horizontalArrangement` before `verticalAlignment`
7. Button/FAB/IconButton/TextButton: `onClick, modifier, ...`
8. Card: `onClick, modifier, shape, ...`
9. CenterAlignedTopAppBar: `title, modifier, navigationIcon, ...`
