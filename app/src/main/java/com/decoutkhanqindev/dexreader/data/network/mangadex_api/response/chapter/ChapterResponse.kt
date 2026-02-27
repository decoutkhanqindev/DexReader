package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.relationship.RelationshipResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterResponse(
  @field:Json(name = "id") val id: String, // UUID của chương
  @field:Json(name = "type") val type: String? = null, // "chapter"
  @field:Json(name = "attributes") val attributes: ChapterAttributesResponse? = null,
  @field:Json(name = "relationships") val relationships: List<RelationshipResponse?>? = null,
)
