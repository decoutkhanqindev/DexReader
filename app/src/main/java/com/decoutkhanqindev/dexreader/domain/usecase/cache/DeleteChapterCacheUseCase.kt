package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class DeleteChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Unit> =
    runSuspendResultCatching {
      repository.deleteChapterCache(chapterId)
    }
}