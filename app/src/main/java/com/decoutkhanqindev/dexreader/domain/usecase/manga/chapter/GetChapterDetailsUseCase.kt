package com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import javax.inject.Inject

class GetChapterDetailsUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
) {
  suspend operator fun invoke(chapterId: String): Result<Chapter> =
    mangaRepository.getChapterDetails(chapterId)
}