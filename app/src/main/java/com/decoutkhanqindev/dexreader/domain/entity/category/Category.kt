package com.decoutkhanqindev.dexreader.domain.entity.category

import com.decoutkhanqindev.dexreader.domain.value.category.CategoryType

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
