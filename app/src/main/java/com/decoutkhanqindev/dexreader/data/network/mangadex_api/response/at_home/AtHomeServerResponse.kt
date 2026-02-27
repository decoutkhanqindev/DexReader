package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.at_home

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AtHomeServerResponse(
  @field:Json(name = "baseUrl") val baseUrl: String? = null,
  @field:Json(name = "chapter") val chapter: ChapterDataResponse? = null,
)
