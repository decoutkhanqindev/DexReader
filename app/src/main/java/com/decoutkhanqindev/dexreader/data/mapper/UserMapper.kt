package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import com.decoutkhanqindev.dexreader.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User =
  User(
    id = uid,
    name = displayName ?: "",
    email = email ?: "",
    profilePictureUrl = photoUrl?.toString()
  )

fun UserProfileDto.toDomain(): User =
  User(
    id = id,
    name = name,
    email = email,
    profilePictureUrl = profilePictureUrl
  )

fun User.toUserProfileDto(): UserProfileDto =
  UserProfileDto(
    id = id,
    name = name,
    email = email,
    profilePictureUrl = profilePictureUrl
  )