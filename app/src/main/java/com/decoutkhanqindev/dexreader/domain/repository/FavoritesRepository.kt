package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
  fun observeFavorites(userId: String): Flow<Result<List<FavoriteManga>>>
  suspend fun addToFavorites(userId: String, manga: FavoriteManga): Result<Unit>
  suspend fun removeFromFavorites(userId: String, mangaId: String): Result<Unit>
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Result<Boolean>>
}