package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : MangaRepository {
  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getLatestUpdateMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getTrendingMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getTrendingMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getNewReleaseMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getTopRatedMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getTopRatedMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getMangaDetails(mangaId: String): Manga =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getMangaDetails(mangaId).data?.toManga(uploadUrl)
          ?: throw BusinessException.Resource.MangaNotFound()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun searchManga(
    query: String,
    offset: Int,
    limit: Int,
  ): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.searchManga(query = query, offset = offset, limit = limit).data?.map {
          it.toManga(uploadUrl)
        }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )
}
