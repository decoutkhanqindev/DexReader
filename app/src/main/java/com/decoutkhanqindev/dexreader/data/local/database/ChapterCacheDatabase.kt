package com.decoutkhanqindev.dexreader.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.parser.RoomDBTypeConverter

@Database(
  entities = [ChapterCacheEntity::class],
  version = 2,
  exportSchema = false
)
@TypeConverters(RoomDBTypeConverter::class)
abstract class ChapterCacheDatabase : RoomDatabase() {
  abstract fun chapterCacheDao(): ChapterCacheDao

  companion object {
    private const val DATABASE_NAME = "chapter_cache.db"

    @Volatile
    private var Instance: ChapterCacheDatabase? = null

    fun getInstance(context: Context): ChapterCacheDatabase =
      // Double-checked locking
      Instance ?: synchronized(this) {
        Instance ?: buildDatabase(context).also { Instance = it }
      }

    private fun buildDatabase(context: Context): ChapterCacheDatabase =
      Room.databaseBuilder(
        context = context,
        klass = ChapterCacheDatabase::class.java,
        name = DATABASE_NAME
      )
        .fallbackToDestructiveMigration(false)
        .build()
  }
}
