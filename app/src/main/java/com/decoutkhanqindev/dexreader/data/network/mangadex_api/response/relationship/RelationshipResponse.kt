package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.relationship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelationshipResponse(
  @field:Json(name = "id") val id: String,
  @field:Json(name = "type")
  val type: String? = null, // "cover_art", "author", "artist", "scanlation_group"
  @field:Json(name = "attributes")
  val attributes: RelationshipAttributesResponse? = null,
)
