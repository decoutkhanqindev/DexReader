package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagAttributesResponse(
  @field:Json(name = "name") val name: Map<String, String?>? = null, // Tên thể loại
  @field:Json(name = "group") val group: String? = null, // Nhóm thể loại
)
