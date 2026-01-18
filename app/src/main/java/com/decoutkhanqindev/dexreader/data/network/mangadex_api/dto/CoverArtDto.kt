package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverArtDto(
  @field:Json(name = "id") val id: String, // UUID của ảnh bìa
  @field:Json(name = "type") val type: String, // "cover_art"
  @field:Json(name = "attributes") val attributes: CoverArtAttributesDto,
)

@JsonClass(generateAdapter = true)
data class CoverArtAttributesDto(
  @field:Json(name = "fileName") val fileName: String, // Tên file ảnh bìa
)
