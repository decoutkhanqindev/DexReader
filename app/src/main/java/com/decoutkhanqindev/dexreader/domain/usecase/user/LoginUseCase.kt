package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class LoginUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    email: String,
    password: String,
  ): Result<Unit> = runSuspendCatching {
    User.validateEmail(email)
    User.validatePassword(password)
    repository.login(email, password)
  }
}
