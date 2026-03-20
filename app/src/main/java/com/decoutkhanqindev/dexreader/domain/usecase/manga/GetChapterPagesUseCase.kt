package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetChapterPagesUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String, mangaId: String): Result<ChapterPages> =
    runSuspendResultCatching { repository.getChapterPages(chapterId, mangaId) }
}
