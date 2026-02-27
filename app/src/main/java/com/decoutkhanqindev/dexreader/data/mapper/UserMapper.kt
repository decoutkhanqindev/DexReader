package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.UserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.UserProfileResponse
import com.decoutkhanqindev.dexreader.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User =
  User(
    id = uid,
    name = displayName ?: "",
    email = email ?: "",
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
