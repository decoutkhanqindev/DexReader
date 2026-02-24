package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.ReadingHistoryDto
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

fun ReadingHistoryDto.toDomain() =
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

fun ReadingHistory.toDto() =
  ReadingHistoryDto(
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
