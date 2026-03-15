package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.user.User
import com.decoutkhanqindev.dexreader.presentation.model.UserUiModel

object UserUiMapper {
  fun User.toUserUiModel() =
    UserUiModel(
      id = id,
      name = name,
      email = email,
      avatarUrl = avatarUrl,
    )

  fun UserUiModel.toDomainUser() =
    User(
      id = id,
      name = name,
      email = email,
      avatarUrl = avatarUrl,
    )
}
