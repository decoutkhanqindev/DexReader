# Blockers

## Linter revert — compile mismatch in Content files

The linter reverted all 3 `*Content.kt` files to an older state with `isError`/`isSuccess`
boolean flags and `onDismissError`. ViewModels/UiStates still use `AuthDialogState`.
Build is likely broken.

**Before implementing one-time events:**
Read `LoginUiState.kt` + `LoginViewModel.kt` + `LoginContent.kt` to confirm ground truth,
then decide:
- Option A: Re-apply `AuthDialogState` pattern to Content files (fight the linter)
- Option B: Accept linter state → revert UiStates/VMs to booleans first

The one-time event plan in `rosy-napping-whale.md` assumes `AuthDialogState` is in place.

## Notes
- Linter hook auto-adds Timber imports and rewrites `Log.d` → `Timber.tag(TAG).d(...)`.
  This is correct — do not fight it.
