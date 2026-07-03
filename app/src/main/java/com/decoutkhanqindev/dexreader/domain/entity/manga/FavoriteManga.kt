package com.decoutkhanqindev.dexreader.domain.entity.manga

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus

data class FavoriteManga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: MangaStatus,
  val addedAt: Long?,
  val rating: Double? = null,
  val follows: Long? = null,
) {
  companion object {
    fun mergeStats(list: List<FavoriteManga>, stats: List<MangaStats>): List<FavoriteManga> {
      if (list.isEmpty() || stats.isEmpty()) return list
      val statsMap = stats.associateBy { it.mangaId }
      return list.map { manga ->
        val entry = statsMap[manga.id]
        manga.copy(rating = entry?.rating, follows = entry?.follows)
      }
    }
  }
}
