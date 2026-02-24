package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    email: String,
    password: String,
    confirmPassword: String,
    name: String,
  ): Result<Unit> = runSuspendResultCatching {
    User.validateEmail(email)
    User.validatePassword(password)
    User.validateConfirmPassword(password, confirmPassword)
    User.validateName(name)
    repository.register(email, password, name)
  }
}
