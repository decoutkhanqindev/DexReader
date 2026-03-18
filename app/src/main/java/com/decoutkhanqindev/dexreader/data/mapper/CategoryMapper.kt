package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.value.category.CategoryType

object CategoryMapper {

  private const val LANG_EN = "en"

  private fun String.toCategoryType() =
    CategoryType.entries.firstOrNull {
      it.name.equals(other = this, ignoreCase = true)
    } ?: CategoryType.UNKNOWN

  fun TagResponse.toCategory() =
    Category(
      id = id,
      title = attributes?.name?.get(LANG_EN)
        ?: attributes?.name?.values?.firstOrNull() ?: Category.DEFAULT_TITLE,
      type = attributes?.group?.toCategoryType() ?: Category.DEFAULT_TYPE
    )
}
