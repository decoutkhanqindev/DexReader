package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.Chapter
import com.decoutkhanqindev.dexreader.presentation.model.ChapterUiModel

object ChapterUiMapper {
  fun Chapter.toChapterUiModel() =
    ChapterUiModel(
      id = id,
      mangaId = mangaId,
      title = title,
      number = number,
      volume = volume,
      publishedAt = publishedAt,
    )
}
