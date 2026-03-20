package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class AddChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(chapterPages: ChapterPages): Result<Unit> =
    runSuspendResultCatching { repository.addChapterCache(chapterPages) }
}
