package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
  private val userRepository: UserRepository,
) {
  operator fun invoke(userId: String): Flow<Result<User?>> =
    userRepository.observeUserProfile(userId)
}