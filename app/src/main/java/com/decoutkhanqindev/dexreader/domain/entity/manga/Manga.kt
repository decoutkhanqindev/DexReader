package com.decoutkhanqindev.dexreader.domain.entity.manga

import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus

data class Manga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val description: String?,
  val author: String?,
  val artist: String?,
  val categories: List<Category>,
  val status: MangaStatus,
  val contentRating: MangaContentRating,
  val year: String?,
  val availableLanguages: List<MangaLanguage>,
  val latestChapter: String?,
  val updatedAt: Long?,
) {
  companion object {
    const val DEFAULT_TITLE = "Untitled"
    const val DEFAULT_DESCRIPTION = "No description"
    const val DEFAULT_AUTHOR = "Unknown"
    const val DEFAULT_ARTIST = "Unknown"
    const val DEFAULT_YEAR = "Unknown"
    const val DEFAULT_LAST_CHAPTER = "Unknown"
  }
}
