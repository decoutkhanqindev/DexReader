package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.ChapterDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDetailsResponse(
  @field:Json(name = "data") val data: ChapterDto,
)
