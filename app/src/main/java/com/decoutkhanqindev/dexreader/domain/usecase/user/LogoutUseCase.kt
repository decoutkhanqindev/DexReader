package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.suspendRunCatching
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(): Result<Unit> = suspendRunCatching { repository.logout() }
}
