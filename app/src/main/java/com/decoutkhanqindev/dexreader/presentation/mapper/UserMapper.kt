package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.user.User
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel

object UserMapper {
  fun User.toUserModel() =
    UserModel(
      id = id,
      name = name,
      email = email,
      avatarUrl = avatarUrl,
    )

  fun UserModel.toDomainUser() =
    User(
      id = id,
      name = name,
      email = email,
      avatarUrl = avatarUrl,
    )
}
