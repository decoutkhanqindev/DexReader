package com.decoutkhanqindev.dexreader.domain.model

data class Chapter(
  val id: String,
  val mangaId: String,
  val title: String,
  val number: String,
  val volume: String,
  val publishedAt: String,
  val language: MangaLanguage,
) {
  companion object {
    const val DEFAULT_MANGA_ID = "0"
    const val DEFAULT_TITLE = "Untitled"
    const val DEFAULT_CHAPTER_NUMBER = "0"
    const val DEFAULT_VOLUME = "0"
    val DEFAULT_LANGUAGE = MangaLanguage.ENGLISH
  }
}
