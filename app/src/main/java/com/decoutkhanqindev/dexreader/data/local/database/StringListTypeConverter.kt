package com.decoutkhanqindev.dexreader.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object StringListTypeConverter {
  private val json = Json { ignoreUnknownKeys = true }

  @TypeConverter
  fun fromStringList(list: List<String>?): String? = list?.let { json.encodeToString(it) }

  @TypeConverter
  fun toStringList(jsonString: String?): List<String>? =
    jsonString?.let {
      try {
        json.decodeFromString<List<String>>(it)
      } catch (_: SerializationException) {
        emptyList()
      }
    }
}