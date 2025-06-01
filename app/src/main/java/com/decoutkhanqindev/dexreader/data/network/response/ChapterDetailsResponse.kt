package com.decoutkhanqindev.dexreader.data.network.response

import com.decoutkhanqindev.dexreader.data.network.dto.ChapterDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDetailsResponse(
  @Json(name = "data")
  val data: ChapterDto,
)