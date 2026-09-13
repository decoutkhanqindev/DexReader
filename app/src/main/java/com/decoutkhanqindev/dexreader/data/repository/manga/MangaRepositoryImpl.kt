package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.withContextCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  private val dataStoreManager: DataStoreManager,
) : MangaRepository {
  private suspend fun List<MangaResponse>?.toMangaList(): List<Manga> {
    val preferredLanguage = dataStoreManager.selectedLangCode.filterNotNull().first().toMangaLanguage()
    return this?.mapNotNull {
      it.toManga(uploadUrl = BuildConfig.UPLOAD_URL, preferredLanguage = preferredLanguage)
    } ?: emptyList()
  }

  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        apiService.getLatestUpdateMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTrendingMangaList(): List<Manga> =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        apiService.getTrendingMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        apiService.getNewReleaseMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getTopRatedMangaList(): List<Manga> =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        apiService.getTopRatedMangaList().data
          .toMangaList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getMangaDetails(mangaId: String): Manga =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        apiService.getMangaDetails(mangaId).data?.toManga(
          uploadUrl = BuildConfig.UPLOAD_URL,
          preferredLanguage = dataStoreManager.selectedLangCode.filterNotNull().first().toMangaLanguage(),
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
    withContextCatching(
      context = Dispatchers.IO,
      action = {
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
