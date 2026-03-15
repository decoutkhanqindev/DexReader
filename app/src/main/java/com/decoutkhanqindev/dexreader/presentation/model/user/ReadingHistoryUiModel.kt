package com.decoutkhanqindev.dexreader.presentation.model.user

import androidx.compose.runtime.Immutable

@Immutable
data class ReadingHistoryUiModel(
  val id: String,
  val mangaId: String,
  val mangaTitle: String,
  val mangaCoverUrl: String,
  val chapterId: String,
  val chapterTitle: String,
  val chapterNumber: String,
  val chapterVolume: String,
  val lastReadPage: Int,
  val pageCount: Int,
  val lastReadAt: String,
)
