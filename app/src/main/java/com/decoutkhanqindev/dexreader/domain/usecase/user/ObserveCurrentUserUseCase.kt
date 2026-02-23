package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  operator fun invoke(): Flow<Result<User?>> = repository.observeCurrentUser().toFlowResult()
}