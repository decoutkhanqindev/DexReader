package com.decoutkhanqindev.dexreader.domain.model

data class Manga(
  val id: String, // UUID từ MangaDex
  val title: String, // Tên truyện
  val coverUrl: String? = null, // URL ảnh bìa
  val description: String? = null, // Mô tả
  val author: String? = null, // Tác giả
  val artist: String? = null, // Họa sĩ
  val genres: List<String> = emptyList(), // Thể loại
  val status: String? = null, // Trạng thái (ongoing, completed,...)
  val lastChapter: String? = null // Chương cuối
)