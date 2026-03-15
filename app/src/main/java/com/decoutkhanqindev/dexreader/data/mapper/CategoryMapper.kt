package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.model.category.Category
import com.decoutkhanqindev.dexreader.domain.model.category.CategoryType

object CategoryMapper {

  private fun String.toCategoryType() =
    CategoryType.entries.firstOrNull {
      it.name.equals(other = this, ignoreCase = true)
    } ?: CategoryType.UNKNOWN

  fun TagResponse.toCategory() =
    Category(
      id = id,
      title = attributes?.name?.get(MangaLanguageCodeParam.ENGLISH.value)
        ?: attributes?.name?.values?.firstOrNull() ?: Category.DEFAULT_TITLE,
      type = attributes?.group?.toCategoryType() ?: Category.DEFAULT_TYPE
    )
}
