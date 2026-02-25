package com.decoutkhanqindev.dexreader.data.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class RoomDBTypeConverters {
  @TypeConverter
  fun fromStringList(list: List<String>?): String? {
    return list?.let { Json.encodeToString(it) }
  }

  @TypeConverter
  fun toStringList(jsonString: String?): List<String>? {
    return jsonString?.let { Json.decodeFromString<List<String>>(it) }
  }
}