package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.FavoriteMangaDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.ReadingHistoryDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreSource {
  suspend fun addAndUpdateUserProfile(userProfile: UserProfileDto)
  fun observeUserProfile(userId: String): Flow<UserProfileDto?>

  fun observeFavorites(
    userId: String,
    limit: Long,
    lastFavoriteMangaId: String? = null,
  ): Flow<List<FavoriteMangaDto>>

  suspend fun addToFavorites(userId: String, manga: FavoriteMangaDto)
  suspend fun removeFromFavorites(userId: String, mangaId: String)
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>

  fun observeHistory(
    userId: String,
    limit: Long,
    mangaId: String? = null,
    lastReadingHistoryId: String? = null,
  ): Flow<List<ReadingHistoryDto>>

  suspend fun addAndUpdateToHistory(userId: String, readingHistory: ReadingHistoryDto)
  suspend fun removeFromHistory(userId: String, readingHistoryId: String)
}