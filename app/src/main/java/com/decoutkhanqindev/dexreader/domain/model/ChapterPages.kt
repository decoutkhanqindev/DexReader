package com.decoutkhanqindev.dexreader.domain.model

data class ChapterPages(
  val chapterId: String,
  val baseUrl: String,
  val dataHash: String,
  val pages: List<String>,
  val totalPages: Int,
) {
  companion object {
    const val DEFAULT_BASE_URL = ""
    const val DEFAULT_HASH = ""
  }
}
