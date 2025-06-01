package com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import jakarta.inject.Inject

class GetChapterPagesUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<ChapterPages> =
    mangaRepository.getChapterPages(chapterId)
}