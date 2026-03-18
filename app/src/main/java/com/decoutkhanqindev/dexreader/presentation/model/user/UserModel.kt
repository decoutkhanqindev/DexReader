package com.decoutkhanqindev.dexreader.presentation.model.user

import androidx.compose.runtime.Immutable

@Immutable
data class UserModel(
  val id: String,
  val name: String,
  val email: String,
  val avatarUrl: String?,
)
