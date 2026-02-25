package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.cover_art

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverArtResponse(
  @field:Json(name = "id") val id: String, // UUID của ảnh bìa
  @field:Json(name = "type") val type: String? = null, // "cover_art"
  @field:Json(name = "attributes") val attributes: CoverArtAttributesResponse? = null,
)
