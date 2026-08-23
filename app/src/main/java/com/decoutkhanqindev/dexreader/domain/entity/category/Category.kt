package com.decoutkhanqindev.dexreader.domain.entity.category

import com.decoutkhanqindev.dexreader.domain.entity.value.category.CategoryType

data class Category(
  val id: String,
  val title: String,
  val description: String?,
  val type: CategoryType,
) {
  companion object {
    const val DEFAULT_TITLE = "Unknown"
    const val DEFAULT_DESCRIPTION = "No description"
    val DEFAULT_TYPE = CategoryType.UNKNOWN
  }
}
