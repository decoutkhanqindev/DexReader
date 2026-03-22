# Session Progress

## Completed This Session (2026-03-22, domain layer review)

### Domain Layer Strict Kotlin Review — 3 parallel agents, all fixes staged in worktrees

#### Critical bug fix
- `UpdateUserProfileUseCase.toFirestoreFlowException()` — `avatarUrl = newAvatarUrl` silently wiped the existing avatar when `newAvatarUrl` was `null` (caller intent: "no change"). Fixed: `val avatarToUpdate = newAvatarUrl ?: currentUser.avatarUrl`.

#### Design correctness fix (exception types)
- `BusinessException` and `InfrastructureException` subtypes were `data class` which generated semantically invalid `equals`/`hashCode`/`copy`/`componentN` for exception types. Changed to plain `class`. The redundant `val rootCause` field (an alias for `Throwable.cause`) was removed; parameter renamed to `cause`. All call sites in `ExceptionMapper` and `UserRepositoryImpl` updated.

#### Architecture fix (testability)
- `ClearExpiredCacheUseCase.clock` was `internal var` — mutable field on immutable use case. Moved to constructor parameter with default `System::currentTimeMillis`.

#### Allocation fix
- `CategoryRepository.getMangaListByCategory()` default parameters `listOf(MangaStatus.ON_GOING)` and `listOf(MangaContentRating.SAFE)` created a new list on every defaulted call. Extracted to companion constants. `GetMangaListByCategoryUseCase` updated to reference the same constants.

#### Domain constant completions
- `ChapterPages`: added `companion object { DEFAULT_BASE_URL = "", DEFAULT_HASH = "" }`
- `Chapter`: added `DEFAULT_LANGUAGE = MangaLanguage.UNKNOWN`
- `Manga`: added `DEFAULT_STATUS = MangaStatus.UNKNOWN`, `DEFAULT_CONTENT_RATING = MangaContentRating.UNKNOWN`
- Simplify pass correctly pruned: `DEFAULT_LAST_UPDATED: Long? = null` (redundant) and `DEFAULT_MANGA_ID = ""` (dead constant)

---

## Still To Do

Awaiting user approval to commit the 3 worktrees:
- `agent-a2c8552b` — entity/exception changes (7 files)
- `agent-abc83fb6` — repository interface changes (2 files)
- `agent-aeba0533` — use case changes (2 files)

No known outstanding issues in the domain layer after these commits land.

## Single Most Important Next Step

Get user go-ahead to commit all 3 worktrees, then merge changes to main branch. After that, domain layer review is complete.
