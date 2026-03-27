No blockers.

## Notes
- A linter hook auto-adds `import timber.log.Timber` and rewrites `Log.d` →
  `Timber.tag(TAG).d(...)` on every ViewModel save. This is correct — Timber is a
  project dependency. Do not fight the hook.
