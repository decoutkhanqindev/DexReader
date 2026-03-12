package com.decoutkhanqindev.dexreader.domain.usecase.user.profile

import com.decoutkhanqindev.dexreader.domain.model.user.User
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  operator fun invoke(): Flow<Result<User?>> = repository.observeCurrentUser().toFlowResult()
}