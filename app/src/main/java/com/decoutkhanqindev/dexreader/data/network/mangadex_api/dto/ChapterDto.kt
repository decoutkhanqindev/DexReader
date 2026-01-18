package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDto(
  @field:Json(name = "id") val id: String, // UUID của chương
  @field:Json(name = "type") val type: String, // "chapter"
  @field:Json(name = "attributes") val attributes: ChapterAttributesDto,
  @field:Json(name = "relationships") val relationships: List<RelationshipDto>? = null,
)

@JsonClass(generateAdapter = true)
data class ChapterAttributesDto(
  @field:Json(name = "title") val title: String? = null, // Tên chương
  @field:Json(name = "chapter") val chapter: String? = null, // Số chương
  @field:Json(name = "volume") val volume: String? = null, // Tập
  @field:Json(name = "publishAt") val publishAt: Long? = null, // Ngày xuất bản
  @field:Json(name = "translatedLanguage") val translatedLanguage: String? = null, // Ngôn ngữ
)
