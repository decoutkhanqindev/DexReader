package com.decoutkhanqindev.dexreader.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object StringListTypeConverter {
  @JvmStatic
  @TypeConverter
  fun fromStringList(list: List<String>): String = Json.encodeToString(list)

  @JvmStatic
  @TypeConverter
  fun toStringList(jsonString: String?): List<String> =
    jsonString?.let {
      try {
        Json.decodeFromString<List<String>>(it)
      } catch (_: SerializationException) {
        emptyList()
      }
    } ?: emptyList()
}
