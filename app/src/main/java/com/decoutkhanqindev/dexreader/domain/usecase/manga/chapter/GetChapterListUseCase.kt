package com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import jakarta.inject.Inject

class GetChapterListUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
) {
  suspend operator fun invoke(
    mangaId: String,
    offset: Int = 0,
    translatedLanguage: String = "en",
    volumeOrder: String = "desc",
    chapterOrder: String = "desc"
  ): Result<List<Chapter>> =
    mangaRepository.getChapterList(
      mangaId = mangaId,
      offset = offset,
      translatedLanguage = translatedLanguage,
      volumeOrder = volumeOrder,
      chapterOrder = chapterOrder
    )
}