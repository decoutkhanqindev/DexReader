package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class AddAndUpdateUserProfileUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(user: User): Result<Unit> =
    runSuspendCatching { repository.addAndUpdateUserProfile(user) }
}