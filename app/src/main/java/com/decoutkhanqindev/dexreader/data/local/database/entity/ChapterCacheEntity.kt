package com.decoutkhanqindev.dexreader.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_cache")
data class ChapterCacheEntity(
  @PrimaryKey val chapterId: String, // UUID từ MangaDex
  @ColumnInfo(name = "mangaId")
  val mangaId: String, // UUID của truyện
  @ColumnInfo(name = "baseUrl")
  val baseUrl: String, // Base URL từ /at-home/server/{chapter_id}
  @ColumnInfo(name = "chapterDataHash")
  val dataHash: String,  //  Hash của dữ liệu chương dùng để tạo URL ảnh
  @ColumnInfo(name = "pageHashes")
  val pageHashes: List<String>, // Danh sách tên file ảnh (hash) của các trang
  @ColumnInfo(name = "totalPages")
  val totalPages: Int, // Tổng số trang
  @ColumnInfo(name = "cachedAt")
  val cachedAt: Long, // Timestamp khi cache
)
