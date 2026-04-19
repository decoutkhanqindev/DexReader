package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue


object ThemeModeMapper {
  fun ThemeMode.toThemeModeValue() =
    when (this) {
      ThemeMode.SYSTEM -> ThemeModeValue.SYSTEM
      ThemeMode.DARK -> ThemeModeValue.DARK
      ThemeMode.LIGHT -> ThemeModeValue.LIGHT
    }

  fun ThemeModeValue.toThemeMode() =
    when (this) {
      ThemeModeValue.SYSTEM -> ThemeMode.SYSTEM
      ThemeModeValue.DARK -> ThemeMode.DARK
      ThemeModeValue.LIGHT -> ThemeMode.LIGHT
    }
}
