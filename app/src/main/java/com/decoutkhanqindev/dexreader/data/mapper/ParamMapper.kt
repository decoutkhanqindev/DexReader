package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaStatusParam
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder

// Enum names are identical across domain and data layers,
// so valueOf(name) is safe and avoids duplicating large when expressions.
fun MangaStatusFilter.toParam(): String =
  MangaStatusParam.valueOf(this.name).value

fun MangaContentRatingFilter.toParam(): String =
  MangaContentRatingParam.valueOf(this.name).value

fun MangaSortOrder.toParam(): String =
  MangaSortOrderParam.valueOf(this.name).value

fun MangaLanguage.toParam(): String =
  MangaLanguageCodeParam.valueOf(this.name).value

// ISO code (from API) → domain enum — for data layer use only
fun String.toMangaLanguage(): MangaLanguage =
  MangaLanguageCodeParam.entries.find { it.value == this }
    ?.let { MangaLanguage.valueOf(it.name) }
    ?: MangaLanguage.UNKNOWN
