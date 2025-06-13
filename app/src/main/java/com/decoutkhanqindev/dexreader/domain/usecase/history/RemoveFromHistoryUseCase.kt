package com.decoutkhanqindev.dexreader.domain.usecase.history

import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import javax.inject.Inject

class RemoveFromHistoryUseCase @Inject constructor(
  private val historyRepository: HistoryRepository
) {
  suspend operator fun invoke(
    userId: String,
    readingHistoryId: String
  ): Result<Unit> =
    historyRepository.removeFromHistory(
      userId = userId,
      readingHistoryId = readingHistoryId
    )
}