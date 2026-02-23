package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    email: String,
    password: String,
    confirmPassword: String,
    name: String,
  ): Result<User> = runSuspendCatching {
    User.validateEmail(email)
    User.validatePassword(password)
    User.validateConfirmPassword(password, confirmPassword)
    User.validateName(name)
    repository.register(email, password)
  }
}
