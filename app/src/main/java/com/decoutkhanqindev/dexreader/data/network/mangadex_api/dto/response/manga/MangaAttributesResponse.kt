package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.manga

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.tag.TagResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaAttributesResponse(
  @field:Json(name = "title") val title: Map<String, String>, // Tên truyện theo ngôn ngữ
  @field:Json(name = "description") val description: Map<String, String>? = null, // Mô tả
  @field:Json(name = "status") val status: String? = null, // Trạng thái
  @field:Json(name = "tags") val tags: List<TagResponse>? = null, // Thể loại
  @field:Json(name = "year") val year: String? = null,
  @field:Json(name = "availableTranslatedLanguages")
  val availableTranslatedLanguages: List<String>? = null, // Ngôn ngữ đã dịch
  @field:Json(name = "lastChapter") val lastChapter: String? = null, // Mới nhất
  @field:Json(name = "updatedAt") val updatedAt: Long? = null, // Ngày cập nhật cuối
)
