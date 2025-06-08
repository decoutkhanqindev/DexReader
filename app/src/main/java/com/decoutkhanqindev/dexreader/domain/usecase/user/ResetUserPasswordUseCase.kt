package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import javax.inject.Inject

class ResetUserPasswordUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(email: String): Result<Unit> =
    userRepository.resetUserPassword(email)
}