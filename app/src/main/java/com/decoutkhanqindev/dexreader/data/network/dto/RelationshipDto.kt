package com.decoutkhanqindev.dexreader.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelationshipDto(
  @Json(name = "id") val id: String,
  @Json(name = "type") val type: String, // "cover_art", "author", "artist", "scanlation_group"
  @Json(name = "attributes") val attributes: RelationshipAttributesDto? = null
)

@JsonClass(generateAdapter = true)
data class RelationshipAttributesDto(
  @Json(name = "name") val name: String? = null, // For author, artist, scanlation_group
  @Json(name = "biography") val biography: Map<String, String>? = null, // For author, artist
  @Json(name = "fileName") val fileName: String? = null, // For cover_art
  @Json(name = "createdAt") val createdAt: Long? = null, // For author, artist, scanlation_group
  @Json(name = "updatedAt") val updatedAt: Long? = null // For author, artist, scanlation_group
)
