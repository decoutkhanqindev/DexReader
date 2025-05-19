package com.decoutkhanqindev.dexreader.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverArtDto(
  @Json(name = "id") val id: String, // UUID của ảnh bìa
  @Json(name = "type") val type: String, // "cover_art"
  @Json(name = "attributes") val attributes: CoverArtAttributesDto
)

@JsonClass(generateAdapter = true)
data class CoverArtAttributesDto(
  @Json(name = "fileName") val fileName: String // Tên file ảnh bìa
)
