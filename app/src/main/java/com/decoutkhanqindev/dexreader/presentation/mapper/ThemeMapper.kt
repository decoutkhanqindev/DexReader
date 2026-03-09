package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.model.ThemeUiModel


object ThemeMapper {
  fun ThemeMode.toThemeUiModel(): ThemeUiModel =
    when (this) {
      ThemeMode.SYSTEM -> ThemeUiModel.SYSTEM
      ThemeMode.DARK -> ThemeUiModel.DARK
      ThemeMode.LIGHT -> ThemeUiModel.LIGHT
    }

  fun ThemeUiModel.toThemeMode(): ThemeMode =
    when (this) {
      ThemeUiModel.SYSTEM -> ThemeMode.SYSTEM
      ThemeUiModel.DARK -> ThemeMode.DARK
      ThemeUiModel.LIGHT -> ThemeMode.LIGHT
    }
}
