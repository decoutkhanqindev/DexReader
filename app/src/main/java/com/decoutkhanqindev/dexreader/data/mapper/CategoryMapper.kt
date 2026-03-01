package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.model.Category

object CategoryMapper {

  fun TagResponse.toCategory(): Category =
    Category(
      id = id,
      title = attributes?.name?.get(MangaLanguageCodeParam.ENGLISH.value)
        ?: attributes?.name?.values?.firstOrNull() ?: "Unknown",
      group = attributes?.group ?: "Unknown"
    )
}
