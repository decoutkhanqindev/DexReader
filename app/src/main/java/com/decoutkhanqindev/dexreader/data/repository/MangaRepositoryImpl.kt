package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MangaRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : MangaRepository {
  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getLatestUpdateMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getTrendingMangaList(): List<Manga> =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getTrendingMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getNewReleaseMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getTopRatedMangaList(): List<Manga> =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getTopRatedMangaList().data.map { it.toDomain(uploadUrl) }
    }

  override suspend fun getMangaDetails(mangaId: String): Manga =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getMangaDetails(mangaId).data.toDomain(uploadUrl)
    }

  override suspend fun searchManga(
    query: String,
    offset: Int,
  ): List<Manga> = withContext(Dispatchers.IO) {
    mangaDexApiService.searchManga(
      query = query,
      offset = offset
    ).data.map { it.toDomain(uploadUrl) }
  }
}
