package com.decoutkhanqindev.dexreader.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long?.toTimeAgo(): String {
  if (this == null) return "Unknown time"

  val now = System.currentTimeMillis()
  val diff = now - this

  return when {
    diff < 60_000L -> "${diff / 1000} seconds ago" // Less than 1 minute
    diff < 3_600_000L -> "${diff / 60_000} minutes ago" // Less than 1 hour
    diff < 86_400_000L -> "${diff / 3_600_000} hours ago" // Less than 1 day
    diff < 604_800_000L -> "${diff / 86_400_000} days ago" // Less than 1 week
    diff < 2_629_746_000L -> "${diff / 604_800_000} weeks ago" // Less than 1 month
    diff < 31_556_926_000L -> "${diff / 2_629_746_000} months ago" // Less than 1 year
    else -> {
      val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
      format.format(Date(this))
    }
  }
}