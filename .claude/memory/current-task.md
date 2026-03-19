# Current Task

## Task

`MenuItem → MenuItemValue` enum refactor — **MOSTLY DONE**, one follow-up needed.

## Status: PENDING — `MenuDrawer.kt` needs to be updated to match `MenuBody.kt`'s new signature

### What was done this session

All 5 planned file changes from the `MenuItem → MenuItemValue` plan were applied:

1. **Created** `presentation/value/menu/MenuItemValue.kt` — enum with `titleRes: Int`,
   `icon: ImageVector`, computed `val id = name.lowercase()`
2. **Deleted** `presentation/screens/common/menu/MenuItem.kt`
3. **Updated** `MenuDrawer.kt` — replaced 6 `stringResource` calls + verbose `remember` block with
   `remember { MenuItemValue.entries.toPersistentList() }`
4. **Updated** `MenuBody.kt` — `ImmutableList<MenuItem>` → `ImmutableList<MenuItemValue>`
5. **Updated** `MenuItemRow.kt` — `item: MenuItem` → `item: MenuItemValue`; resolves title via
   `stringResource(item.titleRes)`

### Post-edit external change

After the refactor, a linter (or external edit) modified `MenuBody.kt` further:

- Changed parameter `selectedItemId: String` → `selectedItem: MenuItemValue`
- Changed comparison `item.id == selectedItemId` → `item == selectedItem`
- Changed `key = { it.id }` → `key = { it }` (enum instances are stable keys)

### Current broken state

`MenuDrawer.kt` still calls `MenuBody(selectedItemId = selectedItemId, ...)` using the old `String`
signature.
`MenuBody` now expects `selectedItem: MenuItemValue`. **This is a compile error.**

### Required follow-up

In `MenuDrawer.kt`:

1. Convert `selectedItemId: String` → find matching `MenuItemValue` entry:
   ```kotlin
   val selectedItem = MenuItemValue.entries.find { it.id == selectedItemId } ?: MenuItemValue.HOME
   ```
2. Pass `selectedItem = selectedItem` to `MenuBody(...)` instead of
   `selectedItemId = selectedItemId`
3. Keep `MenuDrawer`'s own public signature as `selectedItemId: String` (all callers in
   `BaseScreen.kt` / `*Screen.kt` files still use strings — do NOT change them)

### File currently needing edit

`app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/common/menu/MenuDrawer.kt`
— specifically the `val items = ...` block and the `MenuBody(...)` call site
