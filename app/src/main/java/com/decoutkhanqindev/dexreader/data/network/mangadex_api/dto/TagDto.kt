package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagDto(
  @field:Json(name = "id") val id: String,
  @field:Json(name = "attributes") val attributes: TagAttributesDto,
)

@JsonClass(generateAdapter = true)
data class TagAttributesDto(
  @field:Json(name = "name") val name: Map<String, String>? = null, // Tên thể loại
  @field:Json(name = "group") val group: String? = null, // Nhóm thể loại
)
