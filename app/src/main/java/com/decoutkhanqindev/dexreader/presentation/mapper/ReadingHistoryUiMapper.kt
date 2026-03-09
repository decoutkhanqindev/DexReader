package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.model.ReadingHistoryUiModel

object ReadingHistoryUiMapper {
  fun ReadingHistory.toReadingHistoryUiModel(): ReadingHistoryUiModel = ReadingHistoryUiModel(
    id = id,
    mangaId = mangaId,
    mangaTitle = mangaTitle,
    mangaCoverUrl = mangaCoverUrl,
    chapterId = chapterId,
    chapterTitle = chapterTitle,
    chapterNumber = chapterNumber,
    chapterVolume = chapterVolume,
    lastReadPage = lastReadPage,
    pageCount = pageCount,
    lastReadAt = lastReadAt,
  )
}
