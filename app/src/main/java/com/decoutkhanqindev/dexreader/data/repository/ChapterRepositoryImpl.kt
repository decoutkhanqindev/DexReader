package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.ChapterMapper.toChapter
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toApiDomainException
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.domain.exception.MangaException
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ChapterRepositoryImpl
@Inject
constructor(
  private val mangaDexApiService: MangaDexApiService,
) : ChapterRepository {
  override suspend fun getChapterList(
    mangaId: String,
    limit: Int,
    offset: Int,
    language: MangaLanguage,
    sortOrder: MangaSortOrder,
  ): List<Chapter> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getChapterList(
          mangaId = mangaId,
          limit = limit,
          offset = offset,
          translatedLanguages = language.toParam(),
          volumeOrder = sortOrder.toParam(),
          chapterOrder = sortOrder.toParam(),
        )
          .data
          ?.map { it.toChapter() }
          ?: emptyList()
      },
      onCatch = { it.toApiDomainException() }
    )

  override suspend fun getChapterDetails(chapterId: String): Chapter =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getChapterDetails(chapterId).data?.toChapter()
          ?: throw MangaException.ChapterNotFound()
      },
      onCatch = { it.toApiDomainException() }
    )

  override suspend fun getChapterPages(chapterId: String): ChapterPages =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getChapterPages(chapterId).toChapterPages(chapterId)
      },
      onCatch = { it.toApiDomainException() }
    )
}
