# Current Task

## Task
Domain layer audit — Pass 3 (strict-kotlin-reviewer, 5 parallel agents) complete. Fixes reviewed and
partially accepted. 4 staged changes ready to commit. 3 dead-constant additions were caught and
reverted before committing.

## Status: 4 STAGED CHANGES — `./gradlew assembleDebug` → BUILD SUCCESSFUL

---

### What is staged and ready to commit

| File | Change |
|------|--------|
| `domain/repository/manga/CacheRepository.kt` | `clearExpiredCache(expiryTimestamp: Long)` → `clearExpiredCache(olderThan: Long)` |
| `data/repository/manga/CacheRepositoryImpl.kt` | Override updated: `expiryTimestamp` → `olderThan` |
| `domain/repository/manga/MangaRepository.kt` | `searchManga(query, offset, limit)` → added `offset: Int = 0` default (limit already had one) |
| `domain/usecase/manga/GetMangaSuggestionsUseCase.kt` | `private companion object` → `companion object` (inner `private const val` already restricts access) |

### What was REVERTED (dead constants caught by user)

Three entity files were staged by the Pass 3 agents but then reverted because the constants they
added have NO call sites anywhere in the codebase:
- `Chapter.DEFAULT_MANGA_ID = ""` — mapper returns `null` on missing mangaId (early exit), never defaults
- `ChapterPages.DEFAULT_BASE_URL = ""` / `DEFAULT_HASH = ""` — same early-exit pattern in mapper
- `Manga.DEFAULT_STATUS = MangaStatus.UNKNOWN` — `ApiParamMapper.toMangaStatus()` handles UNKNOWN internally
- `Manga.DEFAULT_LAST_UPDATED: Long? = null` — nullable field passed through directly; no default needed

These were reverted via `git checkout HEAD -- <files>`.

### Previously committed work (from sessions earlier today)

The 11-file commit described in previous `current-task.md` WAS committed at the start of this session.
Those changes are now in git history (commits `107ec85`, `bd17fb6`, etc.).

### Suggested commit message for the 4 staged changes

```
fix: domain layer pass 3 — repo contract fixes and use case cleanup

- MangaRepository.searchManga: add offset: Int = 0 default (limit already
  had one; callers supplying offset explicitly had an inconsistent contract)
- CacheRepository/Impl: rename expiryTimestamp -> olderThan (clearer domain
  language — describes the cutoff point, not the arithmetic detail)
- GetMangaSuggestionsUseCase: remove redundant private from companion object
  (inner private const val already restricts access to class scope)
```

---

### Pass 3 — Other agents' findings (no code changes needed)

**Unit 2 (Value types + Exceptions):** All clean.
- All exception subtypes are live (have real throw sites and catch sites)
- `InfrastructureException.ServerUnavailable`, `.Unexpected`, `BusinessException.Resource.AccessDenied` all confirmed live
- Data-layer latent crash noted: `ApiParamMapper.toMangaStatus()` and `toMangaContentRating()` will
  throw `IllegalArgumentException` if called with `MangaStatus.UNKNOWN` or `MangaContentRating.UNKNOWN`
  because neither `MangaStatusParam` nor `MangaContentRatingParam` has an `UNKNOWN` entry. Not fixed
  yet — out of scope for domain audit pass.

**Unit 5 (User/settings use cases):** All clean.
- `UpsertHistoryUseCase` 10 parameters confirmed intentional: ViewModel must not construct
  `ReadingHistory` directly (domain entity isolation boundary). Use case owns the `generateId` and
  `DEFAULT_LAST_READ_AT` logic. This is the correct CA pattern.

---

### Next session

At the start of the next session:
1. Verify `git diff --cached --stat` shows the 4 files above
2. Commit with the message above
3. Then move on to the next layer audit or feature work
