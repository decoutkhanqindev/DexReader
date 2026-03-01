package com.decoutkhanqindev.dexreader.domain.model

data class FavoriteManga(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: String,
  val addedAt: String?,
) {
  companion object {
    const val DEFAULT_ADDED_AT = "Unknown time"
  }
}
