package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.cover_art

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverArtAttributesResponse(
  @field:Json(name = "fileName") val fileName: String? = null, // Tên file ảnh bìa
)
