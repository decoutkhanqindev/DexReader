package com.decoutkhanqindev.dexreader.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object IsoDateTimeMoshiAdapter {

  // SimpleDateFormat is not thread-safe; create a fresh instance per call.
  // "XXX" timezone offset pattern requires API 24+, which matches minSdk.
  private val formatter: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).also {
      it.timeZone = TimeZone.getTimeZone("UTC")
    }

  @FromJson
  fun fromJson(json: String?): Long? {
    if (json == null) return null
    return try {
      formatter.parse(json)?.time
    } catch (e: Exception) {
      null
    }
  }

  @ToJson
  fun toJson(value: Long?): String? {
    if (value == null) return null
    return try {
      formatter.format(Date(value))
    } catch (e: Exception) {
      null
    }
  }
}
