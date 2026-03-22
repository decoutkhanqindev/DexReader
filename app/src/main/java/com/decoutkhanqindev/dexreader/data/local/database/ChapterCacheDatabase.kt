package com.decoutkhanqindev.dexreader.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity

@Database(
  entities = [ChapterCacheEntity::class],
  version = 2,
  exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
abstract class ChapterCacheDatabase : RoomDatabase() {
  abstract fun chapterCacheDao(): ChapterCacheDao
}
