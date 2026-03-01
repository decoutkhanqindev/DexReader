package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.CategoryType

object CategoryMapper {

  private fun String.toCategoryType(): CategoryType =
    CategoryType.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
      ?: CategoryType.UNKNOWN

  fun TagResponse.toCategory(): Category =
    Category(
      id = id,
      title = attributes?.name?.get(MangaLanguageCodeParam.ENGLISH.value)
        ?: attributes?.name?.values?.firstOrNull() ?: Category.DEFAULT_TITLE,
      type = attributes?.group?.toCategoryType() ?: Category.DEFAULT_TYPE
    )
}
