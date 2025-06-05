package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagDto(
  @Json(name = "id") val id: String,
  @Json(name = "attributes") val attributes: TagAttributesDto
)

@JsonClass(generateAdapter = true)
data class TagAttributesDto(
  @Json(name = "name") val name: Map<String, String>? = null, // Tên thể loại
  @Json(name = "description") val description: Map<String, String>? = null,
)