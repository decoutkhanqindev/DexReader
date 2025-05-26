package com.decoutkhanqindev.dexreader.domain.model

data class Manga(
  val id: String, // UUID từ MangaDex
  val title: String, // Tên truyện
  val coverUrl: String, // URL ảnh bìa
  val description: String, // Mô tả
  val author: String, // Tác giả
  val artist: String, // Họa sĩ
  val genres: List<String>, // Thể loại
  val status: String, // Trạng thái (ongoing, completed,...)
  val year: String, // Năm phát hành
  val originalLanguage: String, // Ngôn ngữ
  val availableTranslatedLanguages: List<String>, // Ngôn ngữ đã dịch
  val lastChapter: String, // Chương cuối
  val lastUpdated: String, // Thời gian cập nhật dưới dạng "time ago"
)