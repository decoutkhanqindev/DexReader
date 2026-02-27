package com.decoutkhanqindev.dexreader.data.network.firebase.dto.request

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class UserProfileRequest(
  @get:PropertyName("uid")
  val id: String,

  @get:PropertyName("name")
  val name: String,

  @get:PropertyName("email")
  val email: String,

  @get:PropertyName("profile_picture_url")
  val profilePictureUrl: String?,

  @ServerTimestamp
  val updatedAt: Date? = null,
)
