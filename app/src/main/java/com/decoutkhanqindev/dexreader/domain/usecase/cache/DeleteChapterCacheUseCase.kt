package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import javax.inject.Inject

class DeleteChapterCacheUseCase @Inject constructor(
  private val cacheRepository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Unit> =
    cacheRepository.deleteChapterCache(chapterId)
}