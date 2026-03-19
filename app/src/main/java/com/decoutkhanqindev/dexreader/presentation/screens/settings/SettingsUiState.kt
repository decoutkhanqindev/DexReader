package com.decoutkhanqindev.dexreader.presentation.screens.settings


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.value.settings.ThemeModeValue

@Immutable
data class SettingsUiState(
  val isLoading: Boolean = false,
  val themeOption: ThemeModeValue = ThemeModeValue.SYSTEM,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)