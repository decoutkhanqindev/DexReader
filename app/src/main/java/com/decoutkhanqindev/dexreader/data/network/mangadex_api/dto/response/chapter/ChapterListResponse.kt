package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.chapter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterListResponse(
  @field:Json(name = "data") val data: List<ChapterResponse>? = null,
  @field:Json(name = "limit") val limit: Int? = null,
  @field:Json(name = "offset") val offset: Int? = null,
  @field:Json(name = "total") val total: Int? = null,
)
