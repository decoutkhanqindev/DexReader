package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.ChapterDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterListResponse(
  @field:Json(name = "data") val data: List<ChapterDto>,
  @field:Json(name = "limit") val limit: Int,
  @field:Json(name = "offset") val offset: Int,
  @field:Json(name = "total") val total: Int,
)
