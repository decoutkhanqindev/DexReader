package com.decoutkhanqindev.dexreader.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Chapter(
  val id: String,
  val mangaId: String,
  val title: String,
  val chapterNumber: String,
  val volume: String,
  val publishAt: String,
  val translatedLanguage: String,
)
