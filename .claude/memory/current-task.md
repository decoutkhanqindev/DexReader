# Current Task

## Status: Session ended — home/ and categories/ refactor complete

### Completed this session
- Full re-audit of home/ (5 files): HomeScreen, HomeContent, MangaListSection + NavGraph call site
  - Fixed 7 violations: viewModel first, lambdas after modifier, trailing lambdas, Text style last
- Full audit + fix of categories/ (6 files): CategoriesScreen, CategoriesContent, CategoryTypeSection, CategoryTypeHeader, CategoryList, CategoryItem + NavGraph call site
  - Fixed: viewModel first, lambdas after modifier, trailing lambdas for single-lambda composables, Text style last, Card modifier before shape, NotificationDialog arg order

### Linter behaviour observed
After each save, the linter renames params to shorter canonical names:
- `mangaList` → `items`, `onSelectedManga` → `onItemClick` in home/
- `categoryList` → `items`, `onCategoryClick` → `onItemClick` in categories/
- `onCategoryClick` → `onItemClick` in CategoriesScreen

Always `Read` the file again before the next edit to get the current linter-applied state.

### Next session focus
Continue composable arg ordering refactor with remaining feature screens:
- `presentation/screens/manga_details/`
- `presentation/screens/reader/`
- `presentation/screens/profile/`
- `presentation/screens/settings/`
- `presentation/screens/search/`

Same audit process per directory:
1. Read all .kt files
2. Check definitions: required non-lambda → optional non-modifier → modifier → required lambdas → optional lambdas
3. viewModel FIRST in Screen composables
4. Check call sites mirror definition order
5. Trailing lambda: ≤1 lambda → trailing; >1 same-type → all named; action+content → action named, content trailing
6. Text: style last; Card: onClick first then modifier then shape
