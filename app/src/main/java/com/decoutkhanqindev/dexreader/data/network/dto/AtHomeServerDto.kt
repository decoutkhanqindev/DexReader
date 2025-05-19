package com.decoutkhanqindev.dexreader.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AtHomeServerDto(
  @Json(name = "baseUrl")
  val baseUrl: String,
  @Json(name = "chapter")
  val chapter: ChapterDataDto
)

@JsonClass(generateAdapter = true)
data class ChapterDataDto(
  @Json(name = "hash")
  val hash: String,
  @Json(name = "data")
  val data: List<String>, // Ảnh chất lượng cao
)
