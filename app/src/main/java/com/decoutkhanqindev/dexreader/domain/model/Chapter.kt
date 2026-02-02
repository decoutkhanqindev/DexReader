package com.decoutkhanqindev.dexreader.domain.model

data class Chapter(
  val id: String,
  val mangaId: String,
  val title: String,
  val chapterNumber: String,
  val volume: String,
  val publishAt: String,
  val translatedLanguage: String,
)
