package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDetailsResponse(
  @field:Json(name = "data") val data: ChapterResponse? = null,
)
