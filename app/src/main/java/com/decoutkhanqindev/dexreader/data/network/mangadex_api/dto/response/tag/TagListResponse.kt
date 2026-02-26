package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagListResponse(
  @field:Json(name = "data") val data: List<TagResponse>? = null,
  @field:Json(name = "limit") val limit: Int? = null,
  @field:Json(name = "offset") val offset: Int? = null,
  @field:Json(name = "total") val total: Int? = null,
)
