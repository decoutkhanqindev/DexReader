package com.decoutkhanqindev.dexreader.domain.model

data class ReadingHistory(
  val mangaId: String,
  val mangaTitle: String,
  val mangaCoverUrl: String,
  val mangaAuthor: String,
  val mangaStatus: String,
  val chapterId: String,
  val chapterTitle: String,
  val chapterNumber: String,
  val chapterVolume: String,
  val lastReadPage: String,
  val totalChapterPages: Int,
  val lastReadAt: String
)
