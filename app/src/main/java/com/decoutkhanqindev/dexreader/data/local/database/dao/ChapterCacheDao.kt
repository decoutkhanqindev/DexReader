package com.decoutkhanqindev.dexreader.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity

@Dao
interface ChapterCacheDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addChapterCache(chapterCacheEntity: ChapterCacheEntity)

  @Query("SELECT * FROM chapter_cache WHERE chapterId = :chapterId")
  suspend fun getChapterCache(chapterId: String): ChapterCacheEntity?

  @Query("DELETE FROM chapter_cache WHERE chapterId = :chapterId")
  suspend fun deleteChapterCache(chapterId: String)

  @Query("DELETE FROM chapter_cache WHERE cachedAt < :expiryTimestamp")
  suspend fun clearExpiredCache(expiryTimestamp: Long)
}