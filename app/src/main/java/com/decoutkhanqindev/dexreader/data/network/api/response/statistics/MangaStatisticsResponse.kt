package com.decoutkhanqindev.dexreader.data.network.api.response.statistics

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaStatisticsResponse(
  @field:Json(name = "statistics") val statistics: Map<String, MangaStatisticsEntryResponse>? = null,
)
