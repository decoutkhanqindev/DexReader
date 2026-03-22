package com.decoutkhanqindev.dexreader.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_cache")
data class ChapterCacheEntity(
  @PrimaryKey
  @ColumnInfo(name = "chapterId")
  val chapterId: String,
  @ColumnInfo(name = "mangaId")
  val mangaId: String,
  @ColumnInfo(name = "baseUrl")
  val baseUrl: String,
  @ColumnInfo(name = "chapterDataHash") // column name preserved for DB compatibility; Kotlin property is dataHash
  val dataHash: String,
  @ColumnInfo(name = "pageHashes")
  val pageHashes: List<String>,
  @ColumnInfo(name = "totalPages")
  val totalPages: Int,
  @ColumnInfo(name = "cachedAt")
  val cachedAt: Long,
)
