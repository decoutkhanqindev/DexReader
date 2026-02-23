package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class GetChapterDetailsUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Chapter> =
    runSuspendCatching {
      repository.getChapterDetails(chapterId)
    }
}
