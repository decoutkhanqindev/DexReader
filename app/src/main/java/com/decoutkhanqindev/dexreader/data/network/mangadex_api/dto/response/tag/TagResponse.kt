package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagResponse(
  @field:Json(name = "id") val id: String,
  @field:Json(name = "attributes") val attributes: TagAttributesResponse? = null,
)
