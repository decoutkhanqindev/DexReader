package com.decoutkhanqindev.dexreader.presentation.screens.settings


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.ThemeModeUiModel

@Immutable
data class SettingsUiState(
  val isLoading: Boolean = false,
  val themeOption: ThemeModeUiModel = ThemeModeUiModel.SYSTEM,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)