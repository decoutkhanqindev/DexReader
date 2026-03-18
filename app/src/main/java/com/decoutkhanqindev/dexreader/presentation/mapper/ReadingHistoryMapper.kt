package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.user.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ReadingHistoryMapper {
  fun ReadingHistory.toReadingHistoryModel() =
    ReadingHistoryModel(
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
