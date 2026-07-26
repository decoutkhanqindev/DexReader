package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue

@Immutable
data class SettingsUiState(
  val isLoading: Boolean = false,
  val appliedThemeOption: ThemeModeValue = ThemeModeValue.SYSTEM,
  val selectedThemeOption: ThemeModeValue = ThemeModeValue.SYSTEM,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)
