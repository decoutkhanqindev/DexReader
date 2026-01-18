package com.decoutkhanqindev.dexreader.presentation.screens.profile

import com.decoutkhanqindev.dexreader.domain.model.User

data class ProfileUiState(
  val isLoading: Boolean = false,
  val user: User? = null,
  val updatedName: String? = null,
  val updatedProfilePictureUrl: String? = null,
  val isValidName: Boolean = false,
  val isUpdateUserSuccess: Boolean = false,
  val isLogoutUserSuccess: Boolean = false,
  val isUpdateUserError: Boolean = false,
  val isLogoutUserError: Boolean = false,
)
