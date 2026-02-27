package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.relationship

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelationshipAttributesResponse(
  @field:Json(name = "name") val name: String? = null, // For author, artist, scanlation_group
  @field:Json(name = "biography")
  val biography: Map<String, String?>? = null, // For author, artist
  @field:Json(name = "fileName") val fileName: String? = null, // For cover_art
  @field:Json(name = "createdAt")
  val createdAt: Long? = null, // For author, artist, scanlation_group
  @field:Json(name = "updatedAt")
  val updatedAt: Long? = null, // For author, artist, scanlation_group
)
