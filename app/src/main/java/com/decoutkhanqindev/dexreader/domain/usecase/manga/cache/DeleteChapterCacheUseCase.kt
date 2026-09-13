package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.suspendRunCatching
import javax.inject.Inject

class DeleteChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Unit> =
    suspendRunCatching { repository.deleteChapterCache(chapterId) }
}
