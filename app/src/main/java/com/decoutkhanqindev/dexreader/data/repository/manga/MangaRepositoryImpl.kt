package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  private val settingsRepository: SettingsRepository,
) : MangaRepository {
  private suspend fun List<MangaResponse>?.toMangaList(): List<Manga> {
    val preferredLanguage = settingsRepository.observeContentLanguage().first()
    return this?.mapNotNull {
      it.toManga(uploadUrl = BuildConfig.UPLOAD_URL, preferredLanguage = preferredLanguage)
    } ?: emptyList()
  }

  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getLatestUpdateMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTrendingMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getTrendingMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getNewReleaseMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTopRatedMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getTopRatedMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getMangaDetails(mangaId: String): Manga =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getMangaDetails(mangaId).data?.toManga(
          uploadUrl = BuildConfig.UPLOAD_URL,
          preferredLanguage = settingsRepository.observeContentLanguage().first(),
        )
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
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )
}
