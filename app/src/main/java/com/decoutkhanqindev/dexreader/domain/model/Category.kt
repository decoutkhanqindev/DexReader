package com.decoutkhanqindev.dexreader.domain.model

data class Category(
  val id: String,
  val title: String,
  val type: CategoryType,
) {
  companion object {
    const val DEFAULT_TITLE = "Unknown"
    val DEFAULT_TYPE = CategoryType.UNKNOWN
  }
}
