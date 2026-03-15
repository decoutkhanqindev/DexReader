package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.param.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaStatusParam
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage

object ApiParamMapper {
  fun MangaStatusFilter.toApiParam() =
    MangaStatusParam.valueOf(this.name).value

  fun MangaContentRatingFilter.toApiParam() =
    MangaContentRatingParam.valueOf(this.name).value

  fun MangaSortOrder.toApiParam() =
    MangaSortOrderParam.valueOf(this.name).value

  fun MangaLanguage.toApiParam() =
    MangaLanguageCodeParam.valueOf(this.name).value

  fun String.toMangaLanguage() =
    MangaLanguageCodeParam.entries.find { it.value == this }
      ?.let { MangaLanguage.valueOf(it.name) }
      ?: MangaLanguage.UNKNOWN
}
