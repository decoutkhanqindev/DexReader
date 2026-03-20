package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

/**
 * Retrieves cached [ChapterPages] for the given [chapterId] from local storage.
 *
 * Returns [Result.failure] wrapping
 * [com.decoutkhanqindev.dexreader.domain.exception.BusinessException.Resource.ChapterDataNotFound]
 * when no cache entry exists for [chapterId]. Callers MUST treat this specific failure as a
 * cache-miss signal and fall back to a network fetch via [GetChapterPagesUseCase].
 * All other [Result.failure] cases indicate a genuine storage error and should be surfaced to
 * the user.
 */
class GetChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    runSuspendResultCatching { repository.getChapterCache(chapterId) }
}
