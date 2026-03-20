package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetChapterListUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(
    mangaId: String,
    limit: Int = 20,
    offset: Int = 0,
    language: MangaLanguage = MangaLanguage.ENGLISH,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
  ): Result<List<Chapter>> = runSuspendResultCatching {
    repository.getChapterList(
      mangaId = mangaId,
      limit = limit,
      offset = offset,
      language = language,
      sortOrder = sortOrder,
    )
  }
}
