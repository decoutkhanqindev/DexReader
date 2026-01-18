package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import javax.inject.Inject

class GetChapterCacheUseCase @Inject constructor(
  private val cacheRepository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    cacheRepository.getChapterCache(chapterId)
}