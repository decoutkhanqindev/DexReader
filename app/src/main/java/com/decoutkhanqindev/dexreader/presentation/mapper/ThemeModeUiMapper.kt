package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.settings.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.model.ThemeModeUiModel


object ThemeModeUiMapper {
  fun ThemeMode.toThemeModeUiModel(): ThemeModeUiModel =
    when (this) {
      ThemeMode.SYSTEM -> ThemeModeUiModel.SYSTEM
      ThemeMode.DARK -> ThemeModeUiModel.DARK
      ThemeMode.LIGHT -> ThemeModeUiModel.LIGHT
    }

  fun ThemeModeUiModel.toThemeMode(): ThemeMode =
    when (this) {
      ThemeModeUiModel.SYSTEM -> ThemeMode.SYSTEM
      ThemeModeUiModel.DARK -> ThemeMode.DARK
      ThemeModeUiModel.LIGHT -> ThemeMode.LIGHT
    }
}
