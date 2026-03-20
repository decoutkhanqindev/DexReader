package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.CATEGORIES
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.FAVORITES
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.HISTORY
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.HOME
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.PROFILE
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.SETTINGS

object MenuItemMapper {
  fun MenuItemValue.toNavRoute() = when (this) {
    HOME -> NavRoute.Home
    CATEGORIES -> NavRoute.Categories
    FAVORITES -> NavRoute.Favorites
    HISTORY -> NavRoute.History
    PROFILE -> NavRoute.Profile
    SETTINGS -> NavRoute.Settings
  }
}