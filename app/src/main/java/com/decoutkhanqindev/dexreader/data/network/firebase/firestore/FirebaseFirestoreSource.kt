package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.FavoriteMangaDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreSource {
  suspend fun addUserProfile(userProfile: UserProfileDto): UserProfileDto
  fun observeUserProfile(userId: String): Flow<UserProfileDto?>
  suspend fun updateUserProfile(userProfile: UserProfileDto)
  fun observeFavorites(userId: String): Flow<List<FavoriteMangaDto>>
  suspend fun addToFavorites(userId: String, manga: FavoriteMangaDto)
  suspend fun removeFromFavorites(userId: String, mangaId: String)
  fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean>
}