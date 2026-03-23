package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.UserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.UserProfileResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseUserFirestoreSource {
  suspend fun upsertUserProfile(userProfile: UserProfileRequest)
  fun observeUserProfile(userId: String): Flow<UserProfileResponse?>
}
