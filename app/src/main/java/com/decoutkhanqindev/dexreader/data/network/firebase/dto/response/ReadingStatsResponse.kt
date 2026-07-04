package com.decoutkhanqindev.dexreader.data.network.firebase.dto.response

import com.google.firebase.firestore.PropertyName

data class ReadingStatsResponse(
  @get:PropertyName("user_id")
  @set:PropertyName("user_id")
  var userId: String = "",

  @get:PropertyName("date")
  @set:PropertyName("date")
  var date: String = "",

  @get:PropertyName("duration_millis")
  @set:PropertyName("duration_millis")
  var durationMillis: Long = 0,
)
