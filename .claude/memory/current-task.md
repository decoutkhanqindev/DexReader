# Current Task: Reorder Call Sites of Default Composables in Auth Screens

## Status
In progress — definitions fixed, auth↔auth call sites fixed, now fixing shared composable call sites.

## What
Reorder argument order in call sites of `NotificationDialog`, `SubmitButton`, `ActionButton`,
`Text`, `Icon` within the auth/ directory to match the convention:
  required params → optional params → modifier → required lambdas → optional lambdas → content

## Files being edited
- `presentation/screens/common/dialog/NotificationDialog.kt` — definition needs fixing
- `presentation/screens/common/buttons/SubmitButton.kt` — definition needs fixing
- `presentation/screens/common/buttons/ActionButton.kt` — definition needs fixing
- `login/components/LoginContent.kt` — NotificationDialog call sites
- `login/components/LoginForm.kt` — SubmitButton, Text call sites
- `register/components/RegisterContent.kt` — NotificationDialog call sites
- `register/components/RegisterForm.kt` — SubmitButton, ActionButton, Text call sites
- `forgot_password/components/ForgotPasswordContent.kt` — NotificationDialog call sites
- `forgot_password/components/ForgotPasswordForm.kt` — SubmitButton, ActionButton, Text call sites
- `AuthHeader.kt` — Icon, Text call sites

## Violations remaining (definitions)

**NotificationDialog** — current:
`onConfirmClick` (required lambda, no default) is FIRST, before modifier. Optional params
(`icon`, `title`, `dismiss`, `isEnableDismiss`, `confirm`) are AFTER modifier.
Target:
```
icon, title, dismiss, confirm, isEnableDismiss, modifier, onConfirmClick, onDismissClick
```

**SubmitButton** — current: `onClick` before modifier, `isEnabled` after modifier.
Target: `title, isEnabled, modifier, onClick`

**ActionButton** — current: `onClick` and `content` before modifier, `isEnabled` after modifier.
Target: `isEnabled, modifier, onClick, content`

## Text/Icon pattern to fix
`Text(text=..., style=..., color=..., fontWeight=..., modifier=...)` — modifier should be 2nd
`Icon(painter/imageVector=..., contentDescription=..., tint=..., modifier=...)` — correct, tint optional before modifier

## Last action
Read all 7 auth files to confirm current state. Ready to start editing.
