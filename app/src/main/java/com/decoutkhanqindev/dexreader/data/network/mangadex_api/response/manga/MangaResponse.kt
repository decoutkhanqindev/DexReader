package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.manga

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.relationship.RelationshipResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaResponse(
  @field:Json(name = "id") val id: String, // UUID của truyện
  @field:Json(name = "type") val type: String? = null, // "manga"
  @field:Json(name = "attributes") val attributes: MangaAttributesResponse? = null,
  @field:Json(name = "relationships") val relationships: List<RelationshipResponse>? = null,
)
