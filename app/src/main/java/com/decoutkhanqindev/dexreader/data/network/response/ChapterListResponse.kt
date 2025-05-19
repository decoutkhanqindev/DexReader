package com.decoutkhanqindev.dexreader.data.network.response


import com.decoutkhanqindev.dexreader.data.network.dto.ChapterDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterListResponse(
  @Json(name = "data")
  val data: List<ChapterDto>,
  @Json(name = "limit")
  val limit: Int,
  @Json(name = "offset")
  val offset: Int,
  @Json(name = "total")
  val total: Int
)