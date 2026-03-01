package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.ReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingHistoryResponse
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ReadingHistoryMapper {

  fun ReadingHistoryResponse.toReadingHistory() =
    ReadingHistory(
      id = id,
      mangaId = mangaId,
      mangaTitle = mangaTitle,
      mangaCoverUrl = mangaCoverUrl,
      chapterId = chapterId,
      chapterTitle = chapterTitle,
      chapterNumber = chapterNumber,
      chapterVolume = chapterVolume,
      lastReadPage = lastReadPage,
      totalChapterPages = totalChapterPages,
      lastReadAt = createdAt?.time.toTimeAgo()
    )

  fun ReadingHistory.toReadingHistoryRequest() =
    ReadingHistoryRequest(
      id = id,
      mangaId = mangaId,
      mangaTitle = mangaTitle,
      mangaCoverUrl = mangaCoverUrl,
      chapterId = chapterId,
      chapterTitle = chapterTitle,
      chapterNumber = chapterNumber,
      chapterVolume = chapterVolume,
      lastReadPage = lastReadPage,
      totalChapterPages = totalChapterPages,
    )
}
