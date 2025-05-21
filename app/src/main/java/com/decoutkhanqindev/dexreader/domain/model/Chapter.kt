package com.decoutkhanqindev.dexreader.domain.model

data class Chapter(
  val id: String, // UUID từ MangaDex
  val mangaId: String, // UUID của truyện
  val title: String, // Tên chương
  val chapterNumber: String, // Số chương
  val volume: String, // Tập
  val publishAt: String, // Ngày xuất bản
  val translatedLanguage: String // Ngôn ngữ (en, vi,...)
)
