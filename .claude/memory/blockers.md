# Blockers

## No blockers.

## Notes
- One-time event refactor (`AuthEvent`) dropped — do not resume.
- Linter hook auto-renames params on save — always `Read` before `Edit`.
- Linter may also reorder statements (e.g. moved `LaunchedEffect` above `HorizontalPager`).
- Explore agents are unreliable for code audit — always verify with direct `Read`/`Grep`.
- ActionButton: `isEnabled, modifier, onClick, content` — modifier IS before onClick.
- Non-clickable Card: `modifier` first. Clickable Card: `onClick, modifier, shape, ...`.
- Box: `modifier, contentAlignment, ...`. Column: `modifier, verticalArrangement, horizontalAlignment`.
- DropdownMenuItem: `text, onClick, modifier, ..., leadingIcon, ...`.
- Text: `overflow(12)` before `maxLines(14)`; `style` always last.
