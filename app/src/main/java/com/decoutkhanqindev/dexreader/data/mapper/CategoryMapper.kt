package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiParam
import com.decoutkhanqindev.dexreader.data.mapper.LocalizedTextMapper.localized
import com.decoutkhanqindev.dexreader.data.network.api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.entity.value.category.CategoryType
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage

object CategoryMapper {

  private fun String.toCategoryType() =
    CategoryType.entries.firstOrNull {
      it.name.equals(other = this, ignoreCase = true)
    } ?: CategoryType.UNKNOWN

  fun TagResponse.toCategory(
    preferredLanguage: MangaLanguage = MangaLanguage.ENGLISH,
  ): Category? {
    val languageCode = preferredLanguage.toApiParam()

    return Category(
      id = id ?: return null,
      title = attributes?.name.localized(languageCode) ?: Category.DEFAULT_TITLE,
      description = attributes?.description.localized(languageCode),
      type = attributes?.group?.toCategoryType() ?: Category.DEFAULT_TYPE
    )
  }
}
