# Blockers

## No blockers.

## Notes
- One-time event refactor (`AuthEvent`) dropped — do not resume.
- Batch agents: Bash denied in subagent context + Agent/Skill tool unavailable — coordinator must commit manually.
- Linter hook auto-renames params on save (mangaList→items, onSelectedManga/onCategoryClick→onItemClick) — always `Read` before `Edit`.
- Explore agents are unreliable for code audit — always verify with direct `Read`/`Grep`.
