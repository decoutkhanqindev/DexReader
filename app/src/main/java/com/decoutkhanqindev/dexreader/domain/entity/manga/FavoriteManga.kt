package com.decoutkhanqindev.dexreader.domain.entity.manga

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus

data class FavoriteManga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: MangaStatus,
  val addedAt: Long?,
)
