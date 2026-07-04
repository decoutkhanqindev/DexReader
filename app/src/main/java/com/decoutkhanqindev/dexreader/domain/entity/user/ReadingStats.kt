package com.decoutkhanqindev.dexreader.domain.entity.user

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReadingStats(
  val date: String, // format: yyyy-MM-dd
  val durationMillis: Long,
) {
  companion object {
    fun getCurrentDate(): String {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      return sdf.format(Date())
    }

    fun generateId(userId: String, date: String): String = "${userId}_${date}"
  }
}
