package com.decoutkhanqindev.dexreader.domain.usecase.user.profile

import com.decoutkhanqindev.dexreader.domain.entity.user.User
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  operator fun invoke(userId: String): Flow<Result<User?>> =
    repository.observeUserProfile(userId).toFlowResult()
}