package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
  fun observeFavorites(
    userId: String,
    limit: Int = 20,
    lastFavoriteMangaId: String? = null,
  ): Flow<List<FavoriteManga>>

  suspend fun addToFavorites(userId: String, manga: FavoriteManga)
  suspend fun removeFromFavorites(userId: String, mangaId: String)
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>
}