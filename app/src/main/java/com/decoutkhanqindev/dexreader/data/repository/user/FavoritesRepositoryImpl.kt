package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteManga
import com.decoutkhanqindev.dexreader.data.mapper.FavoriteMangaMapper.toFavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
  private val firestoreSource: FirebaseFirestoreSource,
) : FavoritesRepository {
  override fun observeFavorites(
    userId: String,
    limit: Int,
    lastFavoriteMangaId: String?,
  ): Flow<List<FavoriteManga>> =
    firestoreSource
      .observeFavorites(
        userId = userId,
        limit = limit.toLong(),
        lastFavoriteMangaId = lastFavoriteMangaId
      )
      .map { items -> items.map { it.toFavoriteManga() } }
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addToFavorites(
    userId: String,
    manga: FavoriteManga,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firestoreSource.addToFavorites(userId, manga.toFavoriteMangaRequest())
      },
      onCatch = { it.toFirebaseFirestoreException() }
    )

  override suspend fun removeFromFavorites(
    userId: String,
    mangaId: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { firestoreSource.removeFromFavorites(userId, mangaId) },
      onCatch = { it.toFirebaseFirestoreException() }
    )

  override fun observeIsFavorite(
    userId: String,
    mangaId: String,
  ): Flow<Boolean> =
    firestoreSource
      .observeIsFavorite(userId, mangaId)
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}
