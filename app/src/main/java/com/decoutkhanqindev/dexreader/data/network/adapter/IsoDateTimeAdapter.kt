package com.decoutkhanqindev.dexreader.data.network.adapter

import com.decoutkhanqindev.dexreader.util.TimeAgo.parseIso8601ToEpoch
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object IsoDateTimeAdapter {

  // SimpleDateFormat is not thread-safe; ThreadLocal avoids contention and per-call allocation.
  // "XXX" timezone offset pattern requires API 24+, which matches minSdk.
  private val formatter: ThreadLocal<SimpleDateFormat> = ThreadLocal.withInitial {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).also {
      it.timeZone = TimeZone.getTimeZone("UTC")
    }
  }

  @FromJson
  fun fromJson(json: String?): Long? = json.parseIso8601ToEpoch()

  @ToJson
  fun toJson(value: Long?): String? {
    if (value == null) return null
    return try {
      formatter.get()!!.format(Date(value))
    } catch (e: Exception) {
      null
    }
  }
}
