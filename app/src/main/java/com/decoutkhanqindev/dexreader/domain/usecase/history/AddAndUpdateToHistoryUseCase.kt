package com.decoutkhanqindev.dexreader.domain.usecase.history

import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class AddAndUpdateToHistoryUseCase @Inject constructor(
  private val repository: HistoryRepository,
) {
  suspend operator fun invoke(
    userId: String,
    mangaId: String,
    mangaTitle: String,
    mangaCoverUrl: String,
    chapterId: String,
    chapterTitle: String,
    chapterNumber: String,
    chapterVolume: String,
    lastReadPage: Int,
    pageCount: Int,
  ): Result<Unit> = runSuspendResultCatching {
    val readingHistory = ReadingHistory(
      id = ReadingHistory.generateId(mangaId, chapterId),
      mangaId = mangaId,
      mangaTitle = mangaTitle,
      mangaCoverUrl = mangaCoverUrl,
      chapterId = chapterId,
      chapterTitle = chapterTitle,
      chapterNumber = chapterNumber,
      chapterVolume = chapterVolume,
      lastReadPage = lastReadPage,
      pageCount = pageCount,
      lastReadAt = null
    )
    repository.addAndUpdateToHistory(userId, readingHistory)
  }
}