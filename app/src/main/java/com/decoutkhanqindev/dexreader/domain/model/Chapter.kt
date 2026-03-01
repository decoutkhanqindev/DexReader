package com.decoutkhanqindev.dexreader.domain.model

data class Chapter(
  val id: String,
  val mangaId: String,
  val title: String,
  val chapterNumber: String,
  val volume: String,
  val publishAt: String,
  val translatedLanguage: MangaLanguage,
) {
  companion object {
    const val DEFAULT_MANGA_ID = "0"
    const val DEFAULT_TITLE = "Untitled"
    const val DEFAULT_CHAPTER_NUMBER = "0"
    const val DEFAULT_VOLUME = "0"
    val DEFAULT_LANGUAGE = MangaLanguage.ENGLISH
  }
}
