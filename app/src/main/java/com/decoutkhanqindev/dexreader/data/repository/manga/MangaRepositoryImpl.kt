package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
) : MangaRepository {
  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getLatestUpdateMangaList().data?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTrendingMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getTrendingMangaList().data?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getNewReleaseMangaList().data?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTopRatedMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getTopRatedMangaList().data?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getMangaDetails(mangaId: String): Manga =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getMangaDetails(mangaId).data?.toManga(BuildConfig.UPLOAD_URL)
          ?: throw BusinessException.Resource.MangaNotFound()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun searchManga(
    query: String,
    offset: Int,
    limit: Int,
  ): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.searchManga(
          query = query,
          offset = offset,
          limit = limit
        )
          .data
          ?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )
}
