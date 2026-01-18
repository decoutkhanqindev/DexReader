package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import javax.inject.Inject

class AddChapterCacheUseCase @Inject constructor(
  private val cacheRepository: CacheRepository,
) {
  suspend operator fun invoke(
    mangaId: String,
    chapterPages: ChapterPages,
  ): Result<Unit> =
    cacheRepository.addChapterCache(
      mangaId = mangaId,
      chapterPages = chapterPages
    )
}