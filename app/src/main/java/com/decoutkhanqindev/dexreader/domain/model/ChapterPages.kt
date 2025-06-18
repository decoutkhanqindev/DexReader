package com.decoutkhanqindev.dexreader.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterPages(
  val chapterId: String,
  val baseUrl: String, // Base URL từ /at-home/server
  val chapterDataHash: String, //  Hash của dữ liệu chương dùng để tạo URL ảnh
  val pageUrls: List<String>, // URL đầy đủ của các trang
  val totalPages: Int
)
