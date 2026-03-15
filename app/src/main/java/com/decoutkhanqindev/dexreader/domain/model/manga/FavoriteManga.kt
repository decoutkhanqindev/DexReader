package com.decoutkhanqindev.dexreader.domain.model.manga

data class FavoriteManga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: String,
  val addedAt: Long?,
)
