package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteManga
import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl
@Inject
constructor(
  private val firebaseFirestoreSource: FirebaseFirestoreSource,
) : FavoritesRepository {
  override fun observeFavorites(
    userId: String,
    limit: Int,
    lastFavoriteMangaId: String?,
  ): Flow<List<FavoriteManga>> =
    firebaseFirestoreSource
      .observeFavorites(
        userId = userId,
        limit = limit.toLong(),
        lastFavoriteMangaId = lastFavoriteMangaId
      )
      .map { favoriteMangaResponseList ->
        favoriteMangaResponseList.map { it.toFavoriteManga() }
      }
      .catch { e ->
        if (e is FirebaseFirestoreException &&
          e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
        )
          throw BusinessException.Resource.AccessDenied(rootCause = e)
        else throw e
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addToFavorites(
    userId: String,
    manga: FavoriteManga,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firebaseFirestoreSource.addToFavorites(userId, manga.toFavoriteMangaRequest())
      },
      onCatch = { it.toFirestoreException() }
    )

  override suspend fun removeFromFavorites(
    userId: String,
    mangaId: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { firebaseFirestoreSource.removeFromFavorites(userId, mangaId) },
      onCatch = { it.toFirestoreException() }
    )

  override fun observeIsFavorite(
    userId: String,
    mangaId: String,
  ): Flow<Boolean> =
    firebaseFirestoreSource
      .observeIsFavorite(userId, mangaId)
      .catch { e ->
        if (e is FirebaseFirestoreException &&
          e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
        )
          throw BusinessException.Resource.AccessDenied(rootCause = e)
        else throw e
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}
