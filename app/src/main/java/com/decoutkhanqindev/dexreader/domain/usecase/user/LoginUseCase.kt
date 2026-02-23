package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class LoginUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(email: String, password: String): Result<Unit> = runSuspendCatching {
    User.validate(email, password)
    repository.login(email, password)
  }
}
