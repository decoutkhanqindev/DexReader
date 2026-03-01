package com.decoutkhanqindev.dexreader.presentation.screens.profile

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.User

@Immutable
data class ProfileUiState(
  val isLoading: Boolean = false,
  val currentUser: User? = null,
  val newName: String? = null,
  val newAvatarUrl: String? = null,
  val isUpdateUserSuccess: Boolean = false,
  val isLogoutUserSuccess: Boolean = false,
  val isUpdateUserError: Boolean = false,
  val isLogoutUserError: Boolean = false,
)
