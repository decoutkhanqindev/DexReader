package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDto(
  @field:Json(name = "id") val id: String, // UUID của truyện
  @field:Json(name = "type") val type: String, // "manga"
  @field:Json(name = "attributes") val attributes: MangaAttributesDto,
  @field:Json(name = "relationships") val relationships: List<RelationshipDto>? = null,
)

@JsonClass(generateAdapter = true)
data class MangaAttributesDto(
  @field:Json(name = "title") val title: Map<String, String>, // Tên truyện theo ngôn ngữ
  @field:Json(name = "description") val description: Map<String, String>? = null, // Mô tả
  @field:Json(name = "status") val status: String? = null, // Trạng thái
  @field:Json(name = "tags") val tags: List<TagDto>? = null, // Thể loại
  @field:Json(name = "year") val year: String? = null,
  @field:Json(name = "availableTranslatedLanguages")
  val availableTranslatedLanguages: List<String>? = null, // Ngôn ngữ đã dịch
  @field:Json(name = "lastChapter") val lastChapter: String? = null, // URL ảnh bìa
  @field:Json(name = "updatedAt") val updatedAt: Long? = null, // Ngày cập nhật cuối
)
