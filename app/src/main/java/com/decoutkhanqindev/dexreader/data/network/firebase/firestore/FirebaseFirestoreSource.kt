package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.response.FavoriteMangaResponse
import com.decoutkhanqindev.dexreader.data.network.firebase.response.ReadingHistoryResponse
import com.decoutkhanqindev.dexreader.data.network.firebase.response.UserProfileResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreSource {
  suspend fun upsertUserProfile(userProfile: UserProfileResponse)
  fun observeUserProfile(userId: String): Flow<UserProfileResponse?>

  fun observeFavorites(
    userId: String,
    limit: Long,
    lastFavoriteMangaId: String? = null,
  ): Flow<List<FavoriteMangaResponse>>

  suspend fun addToFavorites(userId: String, manga: FavoriteMangaResponse)
  suspend fun removeFromFavorites(userId: String, mangaId: String)
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>

  fun observeHistory(
    userId: String,
    limit: Long,
    mangaId: String? = null,
    lastReadingHistoryId: String? = null,
  ): Flow<List<ReadingHistoryResponse>>

  suspend fun upsertHistory(userId: String, readingHistory: ReadingHistoryResponse)
  suspend fun removeFromHistory(userId: String, readingHistoryId: String)
}