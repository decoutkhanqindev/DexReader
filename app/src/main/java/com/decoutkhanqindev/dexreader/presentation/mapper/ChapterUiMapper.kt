package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.Chapter
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterUiModel
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ChapterUiMapper {
  fun Chapter.toChapterUiModel() =
    ChapterUiModel(
      id = id,
      mangaId = mangaId,
      title = title ?: Chapter.DEFAULT_TITLE,
      number = number ?: Chapter.DEFAULT_CHAPTER_NUMBER,
      volume = volume ?: Chapter.DEFAULT_VOLUME,
      publishedAt = publishedAt.toTimeAgo(),
    )
}
