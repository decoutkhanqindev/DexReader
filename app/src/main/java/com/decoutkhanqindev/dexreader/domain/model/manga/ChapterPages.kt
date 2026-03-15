package com.decoutkhanqindev.dexreader.domain.model.manga

data class ChapterPages(
  val chapterId: String,
  val baseUrl: String,
  val dataHash: String,
  val pages: List<String>,
  val totalPages: Int,
)
