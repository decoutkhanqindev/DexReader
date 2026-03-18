package com.decoutkhanqindev.dexreader.domain.entity.manga

data class FavoriteManga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: MangaStatus,
  val addedAt: Long?,
)
