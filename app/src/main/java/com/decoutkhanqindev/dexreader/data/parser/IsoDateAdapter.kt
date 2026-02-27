package com.decoutkhanqindev.dexreader.data.parser

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object IsoDateAdapter {
  @RequiresApi(Build.VERSION_CODES.O)
  @FromJson
  fun fromJson(json: String?): Long? {
    if (json == null) return null
    return try {
      ZonedDateTime
        .parse(json, DateTimeFormatter.ISO_DATE_TIME)
        .toInstant()
        .toEpochMilli()
    } catch (e: Exception) {
      null
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ToJson
  fun toJson(value: Long?): String? {
    if (value == null) return null
    return try {
      ZonedDateTime
        .ofInstant(java.time.Instant.ofEpochMilli(value), java.time.ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
      null
    }
  }
}
