package com.decoutkhanqindev.dexreader.domain.model

data class Category(
  val id: String,
  val title: String,
  val group: String,
) {
  companion object {
    const val DEFAULT_TITLE = "Unknown"
    const val DEFAULT_GROUP = "Unknown"
  }
}
