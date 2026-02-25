package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.response.UserProfileResponse
import com.decoutkhanqindev.dexreader.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User =
  User(
    id = uid,
    name = displayName ?: "",
    email = email ?: "",
    profilePictureUrl = photoUrl?.toString()
  )

fun UserProfileResponse.toDomain(): User =
  User(
    id = id,
    name = name,
    email = email,
    profilePictureUrl = profilePictureUrl
  )

fun User.toUserProfileResponse(): UserProfileResponse =
  UserProfileResponse(
    id = id,
    name = name,
    email = email,
    profilePictureUrl = profilePictureUrl
  )
