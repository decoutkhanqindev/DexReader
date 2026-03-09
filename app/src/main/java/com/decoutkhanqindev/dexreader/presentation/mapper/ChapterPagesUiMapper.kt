package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.presentation.model.ChapterPagesUiModel

object ChapterPagesUiMapper {
  fun ChapterPages.toChapterPagesUiModel(): ChapterPagesUiModel = ChapterPagesUiModel(
    chapterId = chapterId,
    pageImageUrls = pages.map { page -> "$baseUrl/data/$dataHash/$page" },
    totalPages = totalPages,
  )
}
