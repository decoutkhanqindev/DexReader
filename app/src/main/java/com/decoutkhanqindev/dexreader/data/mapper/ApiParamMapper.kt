package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.param.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaStatusParam
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus

object ApiParamMapper {
  fun MangaStatus.toApiParam(): String =
    MangaStatusParam.entries.find { it.name == name }?.value
      ?: MangaStatusParam.ON_GOING.value

  fun MangaContentRating.toApiParam(): String =
    MangaContentRatingParam.entries.find { it.name == name }?.value
      ?: MangaContentRatingParam.SAFE.value

  fun MangaSortOrder.toApiParam(): String =
    MangaSortOrderParam.entries.find { it.name == name }?.value
      ?: MangaSortOrderParam.DESC.value

  fun MangaLanguage.toApiParam(): String =
    MangaLanguageCodeParam.entries.find { it.name == name }?.value
      ?: MangaLanguageCodeParam.ENGLISH.value

  fun String?.toMangaStatus(): MangaStatus =
    MangaStatusParam.entries.find { it.value == this?.lowercase() }
      ?.let { param ->
        MangaStatus.entries.find { it.name == param.name }
      } ?: MangaStatus.UNKNOWN

  fun String?.toMangaContentRating(): MangaContentRating =
    MangaContentRatingParam.entries.find { it.value == this?.lowercase() }
      ?.let { param ->
        MangaContentRating.entries.find { it.name == param.name }
      } ?: MangaContentRating.UNKNOWN

  fun String?.toMangaLanguage(): MangaLanguage =
    MangaLanguageCodeParam.entries.find { it.value == this?.lowercase() }
      ?.let { param ->
        MangaLanguage.entries.find { it.name == param.name }
      } ?: MangaLanguage.UNKNOWN
}
