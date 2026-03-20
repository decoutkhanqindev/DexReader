package com.decoutkhanqindev.dexreader.domain.entity.manga

data class ChapterPages(
  val chapterId: String,
  val mangaId: String,
  val baseUrl: String,
  val dataHash: String,
  val pages: List<String>,
) {
  val totalPages: Int get() = pages.size
}
