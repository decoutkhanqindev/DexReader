package com.decoutkhanqindev.dexreader.domain.model

data class Manga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val description: String,
  val author: String,
  val artist: String,
  val categories: List<Category>,
  val status: String,
  val year: String,
  val availableTranslatedLanguages: List<MangaLanguage>,
  val lastChapter: String,
  val lastUpdated: String,
)
