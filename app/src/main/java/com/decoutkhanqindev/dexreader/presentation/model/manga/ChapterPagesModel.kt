package com.decoutkhanqindev.dexreader.presentation.model.manga


import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ChapterPagesModel(
  val chapterId: String,
  val pageImageUrls: ImmutableList<String>,
  val totalPages: Int,
)
