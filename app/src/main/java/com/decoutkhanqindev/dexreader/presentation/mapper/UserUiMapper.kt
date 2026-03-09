package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.model.UserUiModel

object UserUiMapper {
  fun User.toUserUiModel(): UserUiModel = UserUiModel(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
  )
}
