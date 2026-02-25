package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetChapterPagesUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    runSuspendResultCatching { repository.getChapterPages(chapterId) }
}
