# Blockers

## No blockers.

## Pending verification
- `./gradlew assembleDebug` not run after this session's changes (DexReaderTheme refactor +
  193 preview wrappings). Run at start of next session to confirm BUILD SUCCESSFUL.

## Notes (carry-forward)
- One-time event refactor (`AuthEvent`) dropped — do not resume.
- Linter hook auto-renames params on save — always `Read` before `Edit`.
- Explore agents are unreliable for code audit — use `Read`/`Grep` directly.
- `ActionButton`: `isEnabled, modifier, onClick, content` — modifier IS before onClick.
- Non-clickable Card: `modifier` first. Clickable Card: `onClick, modifier, shape, ...`.
- `wrap_previews.py` is idempotent — safe to re-run (skips already-wrapped previews).
