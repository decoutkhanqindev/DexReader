package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiParam
import com.decoutkhanqindev.dexreader.data.mapper.ChapterMapper.toChapter
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.domain.entity.manga.Chapter
import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
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
      block = {
        apiService.getChapterList(
          mangaId = mangaId,
          limit = limit,
          offset = offset,
          translatedLanguages = listOf(language.toApiParam()),
          volumeOrder = sortOrder.toApiParam(),
          chapterOrder = sortOrder.toApiParam(),
        )
          .data
          ?.mapNotNull { it.toChapter() }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getChapterDetails(chapterId: String): Chapter =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getChapterDetails(chapterId).data?.toChapter()
          ?: throw BusinessException.Resource.ChapterNotFound()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getChapterPages(chapterId: String, mangaId: String): ChapterPages =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getChapterPages(chapterId).toChapterPages(chapterId, mangaId)
          ?: throw BusinessException.Resource.ChapterDataNotFound()
      },
      catch = { it.toDomainException() }
    )
}
