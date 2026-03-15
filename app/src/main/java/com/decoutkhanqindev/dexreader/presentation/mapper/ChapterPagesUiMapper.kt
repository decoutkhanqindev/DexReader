package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.ChapterPages
import com.decoutkhanqindev.dexreader.presentation.model.ChapterPagesUiModel
import kotlinx.collections.immutable.toPersistentList

object ChapterPagesUiMapper {
  fun ChapterPages.toChapterPagesUiModel() =
    ChapterPagesUiModel(
      chapterId = chapterId,
      pageImageUrls = pages.toPersistentList(),
      totalPages = totalPages,
    )
}
