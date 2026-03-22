package com.decoutkhanqindev.dexreader.data.network.api.response.chapter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterAttributesResponse(
  @field:Json(name = "title") val title: String? = null,
  @field:Json(name = "chapter") val chapter: String? = null,
  @field:Json(name = "volume") val volume: String? = null,
  @field:Json(name = "publishAt") val publishAt: String? = null,
  @field:Json(name = "translatedLanguage") val translatedLanguage: String? = null,
)
