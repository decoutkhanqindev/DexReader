package com.decoutkhanqindev.dexreader.domain.usecase.network

import com.decoutkhanqindev.dexreader.domain.repository.network.NetworkRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetworkAvailabilityUseCase @Inject constructor(
  private val repository: NetworkRepository,
) {
  operator fun invoke(): Flow<Result<Boolean>> = repository.observeIsAvailable().toFlowResult()
}
