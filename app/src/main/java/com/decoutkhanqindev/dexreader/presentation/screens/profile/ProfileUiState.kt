package com.decoutkhanqindev.dexreader.presentation.screens.profile

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.User

@Immutable
data class ProfileUiState(
  val isLoading: Boolean = false,
  val user: User? = null,
  val newName: String? = null,
  val newProfilePictureUrl: String? = null,
  val isValidName: Boolean = false,
  val isUpdateUserSuccess: Boolean = false,
  val isLogoutUserSuccess: Boolean = false,
  val isUpdateUserError: Boolean = false,
  val isLogoutUserError: Boolean = false,
)
