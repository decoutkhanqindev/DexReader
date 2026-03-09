package com.decoutkhanqindev.dexreader.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterUiModel(
  val id: String,
  val mangaId: String,
  val title: String,
  val number: String,
  val volume: String,
  val publishedAt: String,
)
