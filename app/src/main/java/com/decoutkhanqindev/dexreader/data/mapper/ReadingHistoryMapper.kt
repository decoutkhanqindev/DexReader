package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.response.ReadingHistoryResponse
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

fun ReadingHistoryResponse.toDomain() =
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

fun ReadingHistory.toResponse() =
  ReadingHistoryResponse(
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
