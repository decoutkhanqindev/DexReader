package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(): Result<Unit> = runSuspendCatching { repository.logout() }
}
