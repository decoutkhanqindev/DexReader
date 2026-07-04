package com.decoutkhanqindev.dexreader.data.network.firebase.dto.request

import com.google.firebase.firestore.PropertyName

data class ReadingStatsRequest(
  @get:PropertyName("user_id")
  val userId: String,

  @get:PropertyName("date")
  val date: String,

  @get:PropertyName("duration_millis")
  val durationMillis: Long,
)
