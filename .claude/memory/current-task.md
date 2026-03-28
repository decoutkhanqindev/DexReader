# Current Task

## Status: Session ended — common/ refactor complete

### Completed this session
- Audited all 31 composable definitions in `presentation/screens/common/` — all already correct
- Audited all Material/Compose call sites in `common/` for arg ordering + Text style violations
- Fixed 14 files in a single commit (`b048a7e`) covering:
  - Button/FAB: onClick, modifier, enabled, shape, colors
  - AlertDialog: onDismissRequest, confirmButton, modifier, dismissButton, title, text, shape
  - CenterAlignedTopAppBar: title, modifier, navigationIcon, actions, colors
  - NavigationDrawerItem: label, selected, onClick, modifier, icon, shape
  - LazyVerticalGrid: columns first
  - Row: horizontalArrangement before verticalAlignment
  - Text: style always last (moved in all files)

### Next session focus
Continue refactor to the remaining feature screens in `presentation/screens/`:
- `home/`, `categories/`, `manga_details/`, `reader/`, `profile/`, `settings/`, `search/`
- Same rules: definition param order + call-site arg order (including Text style last)

### Note
- Agents don't commit/push — coordinator must commit directly
- Linter hooks auto-apply worktree changes to main tree (check before editing)
- One-time event refactor (`AuthEvent`) dropped — do NOT resume
