package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class AddChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(
    mangaId: String,
    chapterPages: ChapterPages,
  ): Result<Unit> = runSuspendResultCatching {
    repository.addChapterCache(
      mangaId = mangaId,
      chapterPages = chapterPages
    )
  }
}