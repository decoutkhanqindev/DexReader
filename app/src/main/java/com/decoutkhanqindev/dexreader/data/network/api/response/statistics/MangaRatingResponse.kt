package com.decoutkhanqindev.dexreader.data.network.api.response.statistics

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaRatingResponse(
  @field:Json(name = "bayesian") val bayesian: Double? = null,
)
