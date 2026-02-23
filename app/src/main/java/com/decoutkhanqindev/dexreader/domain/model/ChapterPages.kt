package com.decoutkhanqindev.dexreader.domain.model

data class ChapterPages(
  val chapterId: String,
  val baseUrl: String,
  val chapterDataHash: String,
  val pageUrls: List<String>,
  val totalPages: Int,
)
