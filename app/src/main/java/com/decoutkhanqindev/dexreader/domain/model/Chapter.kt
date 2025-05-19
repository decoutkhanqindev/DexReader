package com.decoutkhanqindev.dexreader.domain.model

data class Chapter(
  val id: String, // UUID từ MangaDex
  val mangaId: String, // UUID của truyện
  val title: String? = null, // Tên chương
  val chapterNumber: String? = null, // Số chương
  val volume: String? = null, // Tập
  val publishAt: String? = null, // Ngày xuất bản
  val translatedLanguage: String? = null // Ngôn ngữ (en, vi,...)
)
