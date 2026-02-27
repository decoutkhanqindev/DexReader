package com.decoutkhanqindev.dexreader.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object StringListTypeConverter {
  @TypeConverter
  fun fromStringList(list: List<String>?): String? = list?.let { Json.encodeToString(it) }

  @TypeConverter
  fun toStringList(jsonString: String?): List<String>? =
    jsonString?.let { Json.decodeFromString<List<String>>(it) }
}