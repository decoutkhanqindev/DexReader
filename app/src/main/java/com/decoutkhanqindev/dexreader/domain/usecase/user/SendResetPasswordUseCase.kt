package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class SendResetPasswordUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(email: String): Result<Unit> = runSuspendCatching {
    User.validateEmail(email)
    repository.sendResetPassword(email)
  }
}
