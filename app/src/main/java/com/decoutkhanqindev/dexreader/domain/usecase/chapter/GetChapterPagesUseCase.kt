package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import javax.inject.Inject

class GetChapterPagesUseCase @Inject constructor(
  private val chapterRepository: ChapterRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    chapterRepository.getChapterPages(chapterId)
}
