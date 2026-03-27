# Session Progress

## NetworkDataModule Split (2026-03-23 — COMPLETE ✓)
- [x] `di/network/ApiModule.kt` + `di/network/FirebaseModule.kt` created
- [x] `di/NetworkDataModule.kt` deleted
- [x] `UploadUrlQualifier` import updated in repos

## DI Qualifier Removal (2026-03-23 — COMPLETE ✓)
- [x] All 4 qualifiers removed (`@BaseUrl`, `@UploadUrl`, `@MangaDexApiService`, `@ThemeMode`)
- [x] `./gradlew assembleDebug` — BUILD SUCCESSFUL

## Auth Screens: Compose Performance (2026-03-27 — COMPLETE ✓)
- [x] `UserError.kt` — `@Stable`
- [x] Input fields — file-level KeyboardOptions constants, single `colorScheme` local
- [x] `*Form` composables — decomposed UiState params, no wrapper lambdas
- [x] `*Content` composables — blur remember, structural fix (`RegisterContent`)

## Auth Screens: Strict Kotlin Review Fixes (2026-03-27 — COMPLETE ✓)
- [x] `LoginViewModel`: `userCase`→`useCase` (C1)
- [x] `LoginUiState`: removed `userError` field; `NotFound`→`isError=true` (C2)
- [x] All 3 VMs: Timber logging via linter hook (M1)
- [x] `LoginViewModel`, `RegisterViewModel`: clear password(s) on success (M2)
- [x] All 3 `*Content`: `remember(isLoading, modifier)` (M3)
- [x] All 3 `*Content`: `LoadingScreen(Modifier.fillMaxSize())` (M4)
- [x] All 3 VMs + Content + Screen: `dismissError`/`dismissSuccess`, removed `rememberSaveable` flags (M5)
- [x] All 3 VMs: `update*` only clears own error field — 8 functions (Minor 1)
- [x] `PasswordInputField`: `rememberSaveable`→`remember` (Minor 2)
- [x] `LoginForm`: `minimumInteractiveComponentSize()` on clickable texts (Minor 3)
- [x] `RegisterViewModel`: `RegistrationFailed` comment, `@Inject constructor` inline (Minor 4)
- [x] `./gradlew assembleDebug` — BUILD SUCCESSFUL (42 tasks)

**No remaining items.**
