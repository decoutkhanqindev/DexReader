package com.decoutkhanqindev.dexreader.domain.usecase.chapter

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetChapterListUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(
    mangaId: String,
    limit: Int = 20,
    offset: Int = 0,
    translatedLanguage: String = "en",
    volumeOrder: String = "desc",
    chapterOrder: String = "desc",
  ): Result<List<Chapter>> = runSuspendResultCatching {
    repository.getChapterList(
      mangaId = mangaId,
      limit = limit,
      offset = offset,
      translatedLanguage = translatedLanguage,
      volumeOrder = volumeOrder,
      chapterOrder = chapterOrder
    )
  }
}
