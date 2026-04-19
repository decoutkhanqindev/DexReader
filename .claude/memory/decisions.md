## Archive (2026-03-18 through 2026-04-14)

Key outcomes from earlier sessions:
- Presentation layer domain isolation complete; `*UiModel`/`*UiError` naming.
- `CategoryGroup` sealed → `CategoryType` enum; domain model properties renamed.
- All 8 mappers in `object`; domain models have `companion object` defaults.
- `Chapter.NavPosition` + `determineNavPosition()` extracted to domain.
- `CancellationException` always rethrown — fixed in FavoritesViewModel + HistoryViewModel.
- `ThemeMode` persistence: ordinal → name; Firebase DTO id fields: `@PropertyName` → `@Exclude`.
- `NetworkDataModule` split → `ApiModule.kt` + `FirebaseModule.kt`; DI qualifiers removed.
- Timber added; dialog visibility driven from UiState via `dismissError()`/`dismissSuccess()`.
- `OutlinedTextFieldDefaults.colors()` — call inline, NOT inside `remember {}`.
- One-time event refactor (`AuthEvent`) dropped by user — do not resume.
- Composable param ordering complete for all `presentation/screens/` directories.
- Canonical orders: Button `onClick,modifier,...`; TopAppBar `title,modifier,...`;
  AlertDialog `onDismissRequest,confirmButton,modifier,...`; Row `modifier,arrangement,alignment`;
  non-clickable Card `modifier,shape,...`; Box `modifier,contentAlignment,...`;
  Column `modifier,verticalArrangement,horizontalAlignment`.
- Trailing lambda: `≤1 lambda` → trailing; `>1 same type` → all named; `action+content` → action named, content trailing.
- `viewModel: VM = hiltViewModel()` FIRST in Screen composables (before modifier).
- Linter auto-renames params on save — always `Read` before `Edit`.
- Explore agents hallucinate "OK" — always verify with direct `Read`/`Grep`.
- `strings_lang_generated.xml` merged into `strings.xml`; reorganized into 15 screen-group sections.
- @Preview coverage complete for all non-Screen/ViewModel composables across all 11 screen folders.

---

## 2026-04-19

### DexReaderTheme: hiltViewModel removed → themeOption param
**Decision:** User replaced `hiltViewModel<SettingsViewModel>()` inside `DexReaderTheme` with a
`themeOption: ThemeModeValue = ThemeModeValue.SYSTEM` parameter.
**Why:** Makes `DexReaderTheme` preview-safe without requiring a Hilt component. Callers
(e.g. MainActivity) pass the ViewModel-observed value; previews use the default.
**Rejected:** Guarding with `if (LocalView.current.isInEditMode)` — user preferred the cleaner
param-based approach.

### Python script instead of parallel agents for preview wrapping
**Decision:** Used a single `wrap_previews.py` script rather than 17 parallel worktree agents.
**Why:** Script is re-runnable, auditable, handles edge cases consistently, and doesn't require
merge management. Agents add parallelism overhead with no benefit for a mechanical text transform.
**Script location:** `C:\Android Development\dex_reader\DexReader\wrap_previews.py`
**Result:** 93 files, 193 previews wrapped.
