package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.ChapterMapper.toChapter
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ParamMapper.toParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
) : ChapterRepository {
  override suspend fun getChapterList(
    mangaId: String,
    limit: Int,
    offset: Int,
    language: MangaLanguage,
    sortOrder: MangaSortOrder,
  ): List<Chapter> = withContext(Dispatchers.IO) {
    mangaDexApiService.getChapterList(
      mangaId = mangaId,
      limit = limit,
      offset = offset,
      translatedLanguages = language.toParam(),
      volumeOrder = sortOrder.toParam(),
      chapterOrder = sortOrder.toParam(),
    ).data?.map { it.toChapter() } ?: emptyList()
  }

  override suspend fun getChapterDetails(chapterId: String): Chapter =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getChapterDetails(chapterId).data?.toChapter()
        ?: throw Exception("Chapter details not found")
    }

  override suspend fun getChapterPages(chapterId: String): ChapterPages =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getChapterPages(chapterId).toChapterPages(chapterId)
    }
}
