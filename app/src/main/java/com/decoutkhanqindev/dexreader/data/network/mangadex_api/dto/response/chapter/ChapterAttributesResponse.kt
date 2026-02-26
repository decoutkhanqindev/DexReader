package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.chapter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterAttributesResponse(
  @field:Json(name = "title") val title: String? = null, // Tên chương
  @field:Json(name = "chapter") val chapter: String? = null, // Số chương
  @field:Json(name = "volume") val volume: String? = null, // Tập
  @field:Json(name = "publishAt") val publishAt: Long? = null, // Ngày xuất bản
  @field:Json(name = "translatedLanguage") val translatedLanguage: String? = null, // Ngôn ngữ
)
