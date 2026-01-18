package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AtHomeServerDto(
  @field:Json(name = "baseUrl") val baseUrl: String,
  @field:Json(name = "chapter") val chapter: ChapterDataDto,
)

@JsonClass(generateAdapter = true)
data class ChapterDataDto(
  @field:Json(name = "hash") val hash: String,
  @field:Json(name = "data") val data: List<String>, // Ảnh chất lượng cao
)
