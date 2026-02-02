package com.decoutkhanqindev.dexreader.presentation.screens.settings

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.ThemeType

@Immutable
data class SettingsUiState(
  val isLoading: Boolean = false,
  val themeType: ThemeType = ThemeType.SYSTEM,
  val isChangeThemeSuccess: Boolean = false,
  val isChangeThemeError: Boolean = false,
)
