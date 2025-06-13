package com.decoutkhanqindev.dexreader.domain.usecase.history

import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHistoryUseCase @Inject constructor(
  private val historyRepository: HistoryRepository
) {
  operator fun invoke(
    userId: String,
    limit: Int = 10,
    lastReadingHistoryId: String? = null
  ): Flow<Result<List<ReadingHistory>>> =
    historyRepository.observeHistory(
      userId = userId,
      limit = limit,
      lastReadingHistoryId = lastReadingHistoryId
    )
}