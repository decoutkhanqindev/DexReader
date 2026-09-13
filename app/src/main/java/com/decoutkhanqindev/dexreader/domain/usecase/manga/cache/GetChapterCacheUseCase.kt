package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.suspendRunCatching
import javax.inject.Inject

class GetChapterCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    suspendRunCatching { repository.getChapterCache(chapterId) }
}
