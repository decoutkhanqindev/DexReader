package com.decoutkhanqindev.dexreader.data.network.api.response.statistics

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaStatisticsEntryResponse(
  @field:Json(name = "rating") val rating: MangaRatingResponse? = null,
  @field:Json(name = "follows") val follows: Long? = null,
)
