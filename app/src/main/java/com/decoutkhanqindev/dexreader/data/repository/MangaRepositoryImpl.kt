package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.exception.MangaException
import com.decoutkhanqindev.dexreader.domain.exception.RemoteException
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class MangaRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : MangaRepository {
  override suspend fun getLatestUpdateMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getLatestUpdateMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { e ->
        when (e) {
          is MangaException -> throw e
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )

  override suspend fun getTrendingMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getTrendingMangaList().data?.map { it.toManga(uploadUrl) } ?: emptyList()
      },
      onCatch = { e ->
        when (e) {
          is MangaException -> throw e
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )

  override suspend fun getNewReleaseMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getNewReleaseMangaList().data?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { e ->
        when (e) {
          is MangaException -> throw e
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )

  override suspend fun getTopRatedMangaList(): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getTopRatedMangaList().data?.map { it.toManga(uploadUrl) } ?: emptyList()
      },
      onCatch = { e ->
        when (e) {
          is MangaException -> throw e
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )

  override suspend fun getMangaDetails(mangaId: String): Manga =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getMangaDetails(mangaId).data?.toManga(uploadUrl)
          ?: throw MangaException.NotFound()
      },
      onCatch = { e ->
        when (e) {
          is MangaException -> throw e
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )

  override suspend fun searchManga(
    query: String,
    offset: Int,
  ): List<Manga> = runSuspendCatching(
    context = Dispatchers.IO,
    onExecute = {
      mangaDexApiService.searchManga(
        query = query,
        offset = offset
      ).data?.map { it.toManga(uploadUrl) } ?: emptyList()
    },
    onCatch = { e ->
      when (e) {
        is MangaException -> throw e
        is RemoteException -> throw e
        is HttpException -> throw RemoteException.RequestFailed(cause = e)
        is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
        else -> throw e
      }
    }
  )
}
