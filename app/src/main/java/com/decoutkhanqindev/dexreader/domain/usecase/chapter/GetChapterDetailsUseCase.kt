package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import javax.inject.Inject

class GetChapterDetailsUseCase @Inject constructor(
  private val chapterRepository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Chapter> =
    chapterRepository.getChapterDetails(chapterId)
}