package com.decoutkhanqindev.dexreader.presentation.model


import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ChapterPagesUiModel(
  val chapterId: String,
  val pageImageUrls: ImmutableList<String>,
  val totalPages: Int,
)
