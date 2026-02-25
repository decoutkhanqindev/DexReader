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
    newProfilePicUrl: String?,
  ): Result<Unit> = runSuspendResultCatching {
    val nameToUpdate = newName?.trim() ?: currentUser.name
    User.validateName(nameToUpdate)

    val hasNameChanged = currentUser.name != nameToUpdate
    val hasPicChanged = currentUser.profilePictureUrl != newProfilePicUrl

    if (!hasNameChanged && !hasPicChanged) return@runSuspendResultCatching

    val updatedUser = currentUser.copy(
      name = nameToUpdate,
      profilePictureUrl = newProfilePicUrl
    )

    repository.updateUserProfile(updatedUser)
  }
}