package com.decoutkhanqindev.dexreader.presentation.ui.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeType

data class SettingsUiState(
  val isLoading: Boolean = false,
  val themeType: ThemeType = ThemeType.SYSTEM,
  val isChangeThemeSuccess: Boolean = false,
  val isChangeThemeError: Boolean = false,
)
