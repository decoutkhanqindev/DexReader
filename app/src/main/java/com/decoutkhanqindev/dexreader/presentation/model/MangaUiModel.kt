package com.decoutkhanqindev.dexreader.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class MangaUiModel(
  val id: String,
  val title: String,
  val coverUrl: String,
  val description: String,
  val author: String,
  val artist: String,
  val categories: List<CategoryUiModel>,
  val status: String,
  val year: String,
  val availableLanguages: List<MangaLanguageUiModel>,
  val latestChapter: String,
  val updatedAt: String,
)
