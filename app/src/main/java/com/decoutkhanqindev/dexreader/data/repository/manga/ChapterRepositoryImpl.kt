package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiParam
import com.decoutkhanqindev.dexreader.data.mapper.ChapterMapper.toChapter
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.domain.model.manga.Chapter
import com.decoutkhanqindev.dexreader.domain.model.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ChapterRepositoryImpl
@Inject
constructor(
  private val apiService: ApiService,
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
        apiService.getChapterList(
          mangaId = mangaId,
          limit = limit,
          offset = offset,
          translatedLanguages = language.toApiParam(),
          volumeOrder = sortOrder.toApiParam(),
          chapterOrder = sortOrder.toApiParam(),
        )
          .data
          ?.map { it.toChapter() }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getChapterDetails(chapterId: String): Chapter =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getChapterDetails(chapterId).data?.toChapter()
          ?: throw com.decoutkhanqindev.dexreader.domain.exception
            .BusinessException.Resource.ChapterNotFound()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getChapterPages(chapterId: String): ChapterPages =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { apiService.getChapterPages(chapterId).toChapterPages(chapterId) },
      onCatch = { it.toDomainException() }
    )
}
