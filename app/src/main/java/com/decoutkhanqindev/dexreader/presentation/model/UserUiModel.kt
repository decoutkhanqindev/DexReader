package com.decoutkhanqindev.dexreader.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserUiModel(
  val id: String,
  val name: String,
  val email: String,
  val avatarUrl: String?,
)
