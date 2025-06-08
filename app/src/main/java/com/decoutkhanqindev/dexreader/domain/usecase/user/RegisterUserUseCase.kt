package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(email: String, password: String): Result<Unit> =
    userRepository.registerUser(email, password)
}