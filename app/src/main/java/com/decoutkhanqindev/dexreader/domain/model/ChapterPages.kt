package com.decoutkhanqindev.dexreader.domain.model

data class ChapterPages(
  val chapterId: String, // UUID của chương
  val baseUrl: String, // Base URL từ /at-home/server
  val pageUrls: List<String>, // URL đầy đủ của các trang
  val totalPages: Int // Tổng số trang
)
