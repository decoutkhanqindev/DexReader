package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.at_home

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDataResponse(
  @field:Json(name = "hash") val hash: String? = null,
  @field:Json(name = "data") val data: List<String>? = null, // Ảnh chất lượng cao
)
