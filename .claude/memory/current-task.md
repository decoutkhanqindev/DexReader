# Current Task: One-Time Event Pattern for Auth Screens

## Status
In progress — not yet implemented.

## What
Refactoring auth screens (Login, Register, ForgotPassword) to use `Channel<AuthEvent>`
so navigation fires from the Screen/Route layer, not from Content composables.

## Plan file
`C:\Users\ADMIN\.claude\plans\rosy-napping-whale.md` — full step-by-step plan.

## Last Action
Removed all `@Preview` functions from 3 Content files (success).

## CRITICAL: Linter reverted Content files — compile mismatch exists
After removing previews, the linter reverted all 3 `*Content.kt` to an older state:
- Uses `uiState.isError` / `uiState.isSuccess` booleans (NOT `AuthDialogState`)
- Uses `onDismissError: () -> Unit` (NOT `onDismissDialog`)
- `fun` (NOT `internal fun`)
- `ForgotPasswordContent`: `onNavigateBack` + `onSubmitSuccess` params restored
- `AuthContent` call uses named `content = { ... }` (not trailing lambda)
- ViewModels/UiStates still reference `AuthDialogState` → build is broken

Read ALL files before editing to confirm ground truth.

## Next Steps
1. Create `presentation/screens/auth/AuthEvent.kt`
2. Update 3 ViewModels: add `Channel<AuthEvent>`, update `dismissDialog()`
3. Update 3 Screen files: add `LaunchedEffect(Unit)` for event collection
4. Update 3 Content files: remove success nav callback, simplify success dialog
5. `./gradlew assembleDebug`
