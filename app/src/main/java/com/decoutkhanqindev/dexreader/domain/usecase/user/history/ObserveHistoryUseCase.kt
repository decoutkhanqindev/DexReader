package com.decoutkhanqindev.dexreader.domain.usecase.user.history

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHistoryUseCase @Inject constructor(
  private val repository: HistoryRepository,
) {
  operator fun invoke(
    userId: String,
    limit: Int = 10,
    mangaId: String? = null,
    lastReadingHistoryId: String? = null,
  ): Flow<Result<List<ReadingHistory>>> =
    repository.observeHistory(
      userId = userId,
      limit = limit,
      mangaId = mangaId,
      lastReadingHistoryId = lastReadingHistoryId
    ).toFlowResult()
}
