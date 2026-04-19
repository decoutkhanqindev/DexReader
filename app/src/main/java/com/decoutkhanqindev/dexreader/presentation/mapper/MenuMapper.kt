package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.CATEGORIES
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.FAVORITES
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.HISTORY
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.HOME
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.PROFILE
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue.SETTINGS
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute

object MenuMapper {
  fun MenuValue.toNavRoute() = when (this) {
    HOME -> NavRoute.Home
    CATEGORIES -> NavRoute.Categories
    FAVORITES -> NavRoute.Favorites
    HISTORY -> NavRoute.History
    PROFILE -> NavRoute.Profile
    SETTINGS -> NavRoute.Settings
  }
}