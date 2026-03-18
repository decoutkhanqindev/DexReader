package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.model.settings.ThemeModeModel


object ThemeModeMapper {
  fun ThemeMode.toThemeModeModel() =
    when (this) {
      ThemeMode.SYSTEM -> ThemeModeModel.SYSTEM
      ThemeMode.DARK -> ThemeModeModel.DARK
      ThemeMode.LIGHT -> ThemeModeModel.LIGHT
    }

  fun ThemeModeModel.toThemeMode() =
    when (this) {
      ThemeModeModel.SYSTEM -> ThemeMode.SYSTEM
      ThemeModeModel.DARK -> ThemeMode.DARK
      ThemeModeModel.LIGHT -> ThemeMode.LIGHT
    }
}
