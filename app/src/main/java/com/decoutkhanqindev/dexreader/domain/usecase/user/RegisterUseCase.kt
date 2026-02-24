package com.decoutkhanqindev.dexreader.domain.usecase.user

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    email: String,
    password: String,
    confirmPassword: String,
    name: String,
  ): Result<Unit> = runSuspendCatching {
    User.apply {
      validatePassword(password)
      validateConfirmPassword(password, confirmPassword)
      validateName(name)
    }
    val registeredUser = repository.register(email, password)
    val userProfile = registeredUser.copy(name = name)
    repository.addAndUpdateUserProfile(userProfile)
  }
}
