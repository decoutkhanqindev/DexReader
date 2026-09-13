package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.prefs

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue

@Immutable
data class PrefsData(
  val isLoading: Boolean = false,
  val appliedThemeOption: ThemeModeValue = ThemeModeValue.DARK,
  val selectedThemeOption: ThemeModeValue = ThemeModeValue.DARK,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)
