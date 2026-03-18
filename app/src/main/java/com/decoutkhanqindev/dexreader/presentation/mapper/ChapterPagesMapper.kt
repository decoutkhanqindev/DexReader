package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterPagesModel
import kotlinx.collections.immutable.toPersistentList

object ChapterPagesMapper {
  fun ChapterPages.toChapterPagesModel() =
    ChapterPagesModel(
      chapterId = chapterId,
      pageImageUrls = pages.toPersistentList(),
      totalPages = totalPages,
    )
}
