package com.decoutkhanqindev.dexreader.domain.usecase.history

import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class AddAndUpdateToHistoryUseCase @Inject constructor(
  private val repository: HistoryRepository,
) {
  suspend operator fun invoke(
    userId: String,
    readingHistory: ReadingHistory,
  ): Result<Unit> = runSuspendCatching {
    repository.addAndUpdateToHistory(
      userId = userId,
      readingHistory = readingHistory
    )
  }
}