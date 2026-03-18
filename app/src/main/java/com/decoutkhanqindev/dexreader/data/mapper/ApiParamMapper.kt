package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.param.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaStatusParam
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaStatus

object ApiParamMapper {
  fun MangaStatus.toApiParam() =
    MangaStatusParam.valueOf(this.name).value

  fun MangaStatus.toApiValue(): String =
    MangaStatusParam.entries.find { it.name == this.name }?.value ?: ""

  fun MangaContentRating.toApiParam() =
    MangaContentRatingParam.valueOf(this.name).value

  fun MangaSortOrder.toApiParam() =
    MangaSortOrderParam.valueOf(this.name).value

  fun MangaLanguage.toApiParam() =
    MangaLanguageCodeParam.valueOf(this.name).value

  fun String?.toMangaStatus() =
    MangaStatusParam.entries.find { it.value == this?.lowercase() }
      ?.let { MangaStatus.valueOf(it.name) }
      ?: MangaStatus.UNKNOWN

  fun String?.toMangaContentRating() =
    MangaContentRatingParam.entries.find { it.value == this?.lowercase() }
      ?.let { MangaContentRating.valueOf(it.name) }
      ?: MangaContentRating.UNKNOWN

  fun String.toMangaLanguage() =
    MangaLanguageCodeParam.entries.find { it.value == this }
      ?.let { MangaLanguage.valueOf(it.name) }
      ?: MangaLanguage.UNKNOWN
}
