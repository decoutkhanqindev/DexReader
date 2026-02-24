package com.decoutkhanqindev.dexreader.domain.usecase.history

import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class RemoveFromHistoryUseCase @Inject constructor(
  private val repository: HistoryRepository,
) {
  suspend operator fun invoke(
    userId: String,
    readingHistoryId: String,
  ): Result<Unit> = runSuspendCatching {
    repository.removeFromHistory(
      userId = userId,
      readingHistoryId = readingHistoryId
    )
  }
}