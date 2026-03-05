package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.ThemeMode
import com.decoutkhanqindev.dexreader.presentation.model.ThemeOption


object ThemeMapper {
  fun ThemeMode.toThemeOption(): ThemeOption =
    when (this) {
      ThemeMode.SYSTEM -> ThemeOption.SYSTEM
      ThemeMode.DARK -> ThemeOption.DARK
      ThemeMode.LIGHT -> ThemeOption.LIGHT
    }

  fun ThemeOption.toThemeMode(): ThemeMode =
    when (this) {
      ThemeOption.SYSTEM -> ThemeMode.SYSTEM
      ThemeOption.DARK -> ThemeMode.DARK
      ThemeOption.LIGHT -> ThemeMode.LIGHT
    }
}