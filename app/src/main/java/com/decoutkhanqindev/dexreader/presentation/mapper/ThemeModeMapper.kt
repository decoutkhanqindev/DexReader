package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.value.settings.ThemeModeValue


object ThemeModeMapper {
  fun ThemeMode.toThemeModeEnum() =
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
