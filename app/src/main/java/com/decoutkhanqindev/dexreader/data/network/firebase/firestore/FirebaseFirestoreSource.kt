package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import kotlinx.coroutines.flow.Flow

interface FirebaseFirestoreSource {
  suspend fun addUserProfile(userProfile: UserProfileDto): UserProfileDto
  fun observeUserProfile(userId: String): Flow<UserProfileDto?>
  suspend fun updateUserProfile(userProfile: UserProfileDto)
}