package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    currentUser: User,
    newName: String?,
    newAvatarUrl: String?,
  ): Result<Unit> = runSuspendResultCatching {
    val nameToUpdate = newName?.trim() ?: currentUser.name
    User.validateName(nameToUpdate)

    val hasNameChanged = currentUser.name != nameToUpdate
    val hasAvatarChanged = currentUser.avatarUrl != newAvatarUrl

    if (!hasNameChanged && !hasAvatarChanged) return@runSuspendResultCatching

    val updatedUser = currentUser.copy(
      name = nameToUpdate,
      avatarUrl = newAvatarUrl
    )

    repository.updateUserProfile(updatedUser)
  }
}