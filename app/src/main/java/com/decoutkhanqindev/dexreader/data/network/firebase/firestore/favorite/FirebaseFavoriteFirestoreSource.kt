package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.FavoriteMangaResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseFavoriteFirestoreSource {
  fun observeFavorites(
    userId: String,
    limit: Long,
    lastFavoriteMangaId: String? = null,
  ): Flow<List<FavoriteMangaResponse>>

  suspend fun addToFavorites(userId: String, manga: FavoriteMangaRequest)
  suspend fun removeFromFavorites(userId: String, mangaId: String)
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>
}
