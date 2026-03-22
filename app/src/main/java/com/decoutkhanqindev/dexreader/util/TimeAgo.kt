package com.decoutkhanqindev.dexreader.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeAgo {

  private val displayFormat = ThreadLocal.withInitial {
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
  }

  private val iso8601Format = ThreadLocal.withInitial {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
  }

  fun String?.parseIso8601ToEpoch(): Long? {
    if (this == null) return null
    return try {
      iso8601Format.get()!!.parse(this)?.time
    } catch (_: Exception) {
      null
    }
  }

  fun Long?.toTimeAgo(): String {
    if (this == null) return "Unknown time"

    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
      diff < 60_000L -> {
        if ((diff / 1000) < 0) "Unknown time"
        else "${diff / 1000} seconds ago"
      }
      diff < 3_600_000L -> "${diff / 60_000} minutes ago"
      diff < 86_400_000L -> "${diff / 3_600_000} hours ago"
      diff < 604_800_000L -> "${diff / 86_400_000} days ago"
      diff < 2_629_746_000L -> "${diff / 604_800_000} weeks ago"
      diff < 31_556_926_000L -> "${diff / 2_629_746_000} months ago"
      else -> displayFormat.get()!!.format(Date(this))
    }
  }
}
