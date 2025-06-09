package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import javax.inject.Inject

class AddUserProfileUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(user: User): Result<User> =
    userRepository.addUserProfile(user)
}