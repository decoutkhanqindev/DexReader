package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response


import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.MangaDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaListResponse(
  @Json(name = "data")
  val data: List<MangaDto>,
  @Json(name = "limit")
  val limit: Int,
  @Json(name = "offset")
  val offset: Int,
  @Json(name = "total")
  val total: Int
)