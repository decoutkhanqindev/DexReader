package com.decoutkhanqindev.dexreader.presentation.model.manga

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterModel(
  val id: String,
  val mangaId: String,
  val title: String,
  val number: String,
  val volume: String,
  val publishedAt: String,
)
