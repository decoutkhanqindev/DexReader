package com.decoutkhanqindev.dexreader.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDto(
  @Json(name = "id") val id: String, // UUID của truyện
  @Json(name = "type") val type: String, // "manga"
  @Json(name = "attributes") val attributes: MangaAttributesDto,
  @Json(name = "relationships") val relationships: List<RelationshipDto>? = null
)

@JsonClass(generateAdapter = true)
data class MangaAttributesDto(
  @Json(name = "title") val title: Map<String, String>, // Tên truyện theo ngôn ngữ
  @Json(name = "description") val description: Map<String, String>? = null, // Mô tả
  @Json(name = "status") val status: String? = null, // Trạng thái
  @Json(name = "tags") val tags: List<TagDto>? = null, // Thể loại
  @Json(name = "lastChapter") val lastChapter: String? = null, // Chương cuối
  @Json(name = "lastVolume") val lastVolume: String? = null // Tập cuối
)

@JsonClass(generateAdapter = true)
data class TagDto(
  @Json(name = "id") val id: String,
  @Json(name = "attributes") val attributes: TagAttributesDto
)

@JsonClass(generateAdapter = true)
data class TagAttributesDto(
  @Json(name = "name") val name: Map<String, String>, // Tên thể loại
)
