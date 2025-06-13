package com.decoutkhanqindev.dexreader.domain.model

data class ReadingHistory(
  val id: String,
  val mangaId: String,
  val mangaTitle: String,
  val mangaCoverUrl: String,
  val chapterId: String,
  val chapterTitle: String,
  val chapterNumber: String,
  val chapterVolume: String,
  val lastReadPage: Int,
  val totalChapterPages: Int,
  val lastReadAt: String?,
)
