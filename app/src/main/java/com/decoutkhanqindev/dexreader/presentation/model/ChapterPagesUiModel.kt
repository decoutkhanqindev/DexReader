package com.decoutkhanqindev.dexreader.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterPagesUiModel(
  val chapterId: String,
  val pageImageUrls: List<String>,
  val totalPages: Int,
)
