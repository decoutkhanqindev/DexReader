package com.decoutkhanqindev.dexreader.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelationshipDto(
  @Json(name = "id") val id: String,
  @Json(name = "type") val type: String, // "cover_art", "author", "artist",...
  @Json(name = "attributes") val attributes: Map<String, String>? = null
)
