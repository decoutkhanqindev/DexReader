package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class GetChapterPagesUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    runSuspendCatching {
      repository.getChapterPages(chapterId)
    }
}
