package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService
) : ChapterRepository {
  override suspend fun getChapterList(
    mangaId: String,
    limit: Int,
    offset: Int,
    translatedLanguage: String,
    volumeOrder: String,
    chapterOrder: String
  ): Result<List<Chapter>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getChapterList(
        mangaId = mangaId,
        limit = limit,
        offset = offset,
        translatedLanguages = translatedLanguage,
        volumeOrder = volumeOrder,
        chapterOrder = chapterOrder
      ).data.map { it.toDomain() }
    }

  override suspend fun getChapterDetails(chapterId: String): Result<Chapter> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getChapterDetails(chapterId).data.toDomain()
    }


  override suspend fun getChapterPages(chapterId: String): Result<ChapterPages> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getChapterPages(chapterId).toDomain(chapterId)
    }
}