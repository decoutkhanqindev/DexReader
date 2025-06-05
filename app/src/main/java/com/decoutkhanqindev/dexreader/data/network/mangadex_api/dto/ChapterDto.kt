package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDto(
  @Json(name = "id") val id: String, // UUID của chương
  @Json(name = "type") val type: String, // "chapter"
  @Json(name = "attributes") val attributes: ChapterAttributesDto,
  @Json(name = "relationships") val relationships: List<RelationshipDto>? = null
)

@JsonClass(generateAdapter = true)
data class ChapterAttributesDto(
  @Json(name = "title") val title: String? = null, // Tên chương
  @Json(name = "chapter") val chapter: String? = null, // Số chương
  @Json(name = "volume") val volume: String? = null, // Tập
  @Json(name = "publishAt") val publishAt: Long? = null, // Ngày xuất bản
  @Json(name = "translatedLanguage") val translatedLanguage: String? = null // Ngôn ngữ
)
