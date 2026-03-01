package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteManga
import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.exception.FavoritesHistoryException
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
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
        if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw FavoritesHistoryException.PermissionDenied(cause = e)
        else throw e
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addToFavorites(
    userId: String,
    manga: FavoriteManga,
  ) = withContext(Dispatchers.IO) {
    firebaseFirestoreSource.addToFavorites(userId, manga.toFavoriteMangaRequest())
  }

  override suspend fun removeFromFavorites(
    userId: String,
    mangaId: String,
  ) = withContext(Dispatchers.IO) {
    firebaseFirestoreSource.removeFromFavorites(userId, mangaId)
  }

  override fun observeIsFavorite(
    userId: String,
    mangaId: String,
  ): Flow<Boolean> =
    firebaseFirestoreSource
      .observeIsFavorite(userId, mangaId)
      .catch { e ->
        if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw FavoritesHistoryException.PermissionDenied(cause = e)
        else throw e
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}
