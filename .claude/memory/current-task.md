# Current Task

## Task
Domain layer strict Kotlin double-check review — 3 parallel review agents ran across entity/exception, repository interfaces, and use cases. All fixes applied to isolated git worktrees.

## Status: COMPLETE — awaiting user approval to commit

---

### What was completed this session

**Unit 1 — `domain/entity/` + `domain/exception/` + affected data layer files:**
- `ChapterPages.kt`: added `companion object { DEFAULT_BASE_URL = "", DEFAULT_HASH = "" }`
- `Chapter.kt`: added `DEFAULT_LANGUAGE = MangaLanguage.UNKNOWN`
- `Manga.kt`: added `DEFAULT_STATUS = MangaStatus.UNKNOWN`, `DEFAULT_CONTENT_RATING = MangaContentRating.UNKNOWN`
- `BusinessException.kt`: `data class` → `class` for all subtypes; removed redundant `val rootCause` field; parameter renamed to `cause` (standard `Throwable` idiom)
- `InfrastructureException.kt`: same `data class` → `class` + `rootCause` removal
- `ExceptionMapper.kt`: all `rootCause = this` → `cause = this`
- `UserRepositoryImpl.kt`: all `rootCause = e` → `cause = e`
- Simplify pass removed: `DEFAULT_LAST_UPDATED: Long? = null` (nullable null constant is redundant) and `DEFAULT_MANGA_ID = ""` (dead constant, no call site)

**Unit 2 — `domain/repository/`:**
- `CategoryRepository.kt`: `listOf(MangaStatus.ON_GOING)` and `listOf(MangaContentRating.SAFE)` extracted to `companion object` constants `DEFAULT_STATUS_FILTER` / `DEFAULT_CONTENT_RATING_FILTER`
- `GetMangaListByCategoryUseCase.kt`: updated to reference same constants

**Unit 3 — `domain/usecase/`:**
- `ClearExpiredCacheUseCase.kt`: `internal var clock: () -> Long` → constructor parameter `private val clock: () -> Long = System::currentTimeMillis`
- `UpdateUserProfileUseCase.kt`: **bug fix** — `avatarUrl = newAvatarUrl` was silently wiping the avatar when `newAvatarUrl = null` meant "unchanged"; fixed to `val avatarToUpdate = newAvatarUrl ?: currentUser.avatarUrl`

---

### Pending
- User has not yet approved commits for any of the 3 worktrees
- Worktrees: `agent-a2c8552b`, `agent-abc83fb6`, `agent-aeba0533`
- Once user approves, commit each worktree and merge/cherry-pick changes into main branch
