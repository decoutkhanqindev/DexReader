package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(): Result<Unit> = runSuspendResultCatching { repository.logout() }
}
