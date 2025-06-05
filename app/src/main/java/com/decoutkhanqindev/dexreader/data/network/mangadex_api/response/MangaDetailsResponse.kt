package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.MangaDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDetailsResponse(
  @Json(name = "data")
  val data: MangaDto
)
