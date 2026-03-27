# Blockers

## Linter revert — compile mismatch in Content files (pre-existing)
LoginViewModel/LoginUiState reference `AuthDialogState` but LoginContent was reverted by linter
to use `isError`/`isSuccess` booleans. Build likely broken on `./gradlew assembleDebug`.
Agent confirmed: `Unresolved reference 'dismissError'` in LoginScreen.kt during build.

**Before implementing one-time events:**
Must reconcile LoginUiState + LoginViewModel + LoginContent first.

## Shared composable definitions need reordering (current blocker for this session)

**NotificationDialog** (`common/dialog/NotificationDialog.kt`):
- `onConfirmClick: () -> Unit` is first param (required lambda before modifier — violation)
- `icon`, `title`, `dismiss`, `isEnableDismiss`, `confirm` are all after `modifier` (violation)
- Target: `icon, title, dismiss, confirm, isEnableDismiss, modifier, onConfirmClick, onDismissClick`
- Has ~30 call sites across codebase — auth/ has 6

**SubmitButton** (`common/buttons/SubmitButton.kt`):
- `onClick` required lambda before `modifier`, `isEnabled` optional after `modifier`
- Target: `title, isEnabled, modifier, onClick`

**ActionButton** (`common/buttons/ActionButton.kt`):
- `onClick` and `content` before `modifier`, `isEnabled` after `modifier`
- Target: `isEnabled, modifier, onClick, content`

## Notes
- Linter hook auto-adds Timber imports — correct, do not fight it.
- Linter also reorders Text args to put `modifier` as 2nd arg (after `text`) — correct, follow it.
