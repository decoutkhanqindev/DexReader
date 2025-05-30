package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.network.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

class MangaRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @UploadUrlQualifier private val uploadUrl: String
) : MangaRepository {
  override suspend fun getLatestUpdateMangaList(): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getLatestUpdateMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getTrendingMangaList(): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getTrendingMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getNewReleaseMangaList(): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getNewReleaseMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getCompletedMangaList(): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getCompletedMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getMangaDetails(mangaId: String): Result<Manga> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getMangaDetails(mangaId).data.toDomain(uploadUrl)
    }

  override suspend fun getChapterList(
    mangaId: String,
    offset: Int,
    translatedLanguage: String,
    volumeOrder: String,
    chapterOrder: String
  ): Result<List<Chapter>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getChapterList(
        mangaId = mangaId,
        offset = offset,
        translatedLanguages = translatedLanguage,
        volumeOrder = volumeOrder,
        chapterOrder = chapterOrder
      ).data.map { it.toDomain() }
    }

  override suspend fun getChapterPages(chapterId: String): Result<ChapterPages> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getChapterPages(chapterId).toDomain(chapterId)
    }

  override suspend fun searchManga(
    query: String,
    offset: Int
  ): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.searchManga(
        query = query,
        offset = offset
      ).data.map { it.toDomain(uploadUrl) }
    }
}