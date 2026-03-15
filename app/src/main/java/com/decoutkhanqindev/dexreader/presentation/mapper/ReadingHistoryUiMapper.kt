package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.user.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryUiModel
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ReadingHistoryUiMapper {
  fun ReadingHistory.toReadingHistoryUiModel() =
    ReadingHistoryUiModel(
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
      lastReadAt = lastReadAt.toTimeAgo(),
    )
}
