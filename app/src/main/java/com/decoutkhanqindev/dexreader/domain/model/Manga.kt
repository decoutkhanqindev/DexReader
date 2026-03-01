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
) {
  companion object {
    const val DEFAULT_TITLE = "Untitled"
    const val DEFAULT_DESCRIPTION = "No description"
    const val DEFAULT_AUTHOR = "Unknown"
    const val DEFAULT_ARTIST = "Unknown"
    const val DEFAULT_STATUS = "Unknown"
    const val DEFAULT_YEAR = "Unknown"
    const val DEFAULT_LAST_CHAPTER = "Unknown"
    const val DEFAULT_LAST_UPDATED = ""
  }
}
