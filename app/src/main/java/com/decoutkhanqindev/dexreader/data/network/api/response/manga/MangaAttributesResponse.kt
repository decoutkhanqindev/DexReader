package com.decoutkhanqindev.dexreader.data.network.api.response.manga

import com.decoutkhanqindev.dexreader.data.network.api.response.tag.TagResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaAttributesResponse(
  @field:Json(name = "title") val title: Map<String, String>? = null,
  @field:Json(name = "description") val description: Map<String, String>? = null,
  @field:Json(name = "status") val status: String? = null,
  @field:Json(name = "contentRating") val contentRating: String? = null,
  @field:Json(name = "tags") val tags: List<TagResponse>? = null,
  @field:Json(name = "year") val year: Int? = null,
  @field:Json(name = "availableTranslatedLanguages")
  val availableTranslatedLanguages: List<String>? = null,
  @field:Json(name = "lastChapter") val lastChapter: String? = null,
  @field:Json(name = "updatedAt") val updatedAt: String? = null,
)
