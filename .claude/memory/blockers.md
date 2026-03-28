# Blockers

## No blockers.

## Notes
- One-time event refactor (`AuthEvent`) dropped — do not resume.
- Batch agents: Bash denied in subagent context + Agent/Skill tool unavailable — coordinator must commit manually.
- Linter hook auto-applies worktree changes to main tree — always `Read` before `Edit` to get current state.
- Explore agents are unreliable for code audit — always verify with direct `Read`/`Grep`.
