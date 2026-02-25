package com.decoutkhanqindev.dexreader.data.network.firebase.response

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class UserProfileResponse(
  @get:PropertyName("uid")
  @set:PropertyName("uid")
  var id: String = "",

  @get:PropertyName("name")
  @set:PropertyName("name")
  var name: String = "",

  @get:PropertyName("email")
  @set:PropertyName("email")
  var email: String = "",

  @get:PropertyName("profile_picture_url")
  @set:PropertyName("profile_picture_url")
  var profilePictureUrl: String? = null,

  @ServerTimestamp
  val createdAt: Date? = null,
  @ServerTimestamp
  val updatedAt: Date? = null,
)