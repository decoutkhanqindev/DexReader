package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.UserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.UserProfileResponse
import com.decoutkhanqindev.dexreader.domain.model.User
import com.google.firebase.auth.FirebaseUser

object UserMapper {

  fun FirebaseUser.toUser(): User =
    User(
      id = uid,
      name = displayName ?: User.DEFAULT_NAME,
      email = email ?: User.DEFAULT_EMAIL,
      profilePictureUrl = photoUrl?.toString()
    )

  fun UserProfileResponse.toUser(): User =
    User(
      id = id,
      name = name,
      email = email,
      profilePictureUrl = profilePictureUrl
    )

  fun User.toUserProfileRequest(): UserProfileRequest =
    UserProfileRequest(
      id = id,
      name = name,
      email = email,
      profilePictureUrl = profilePictureUrl
    )
}
