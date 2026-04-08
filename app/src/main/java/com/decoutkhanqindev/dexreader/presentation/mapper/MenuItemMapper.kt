package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.CATEGORIES
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.FAVORITES
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.HISTORY
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.HOME
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.PROFILE
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue.SETTINGS
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination

object MenuItemMapper {
  fun MenuItemValue.toNavRoute() =
    when (this) {
      HOME -> NavDestination.Home
      CATEGORIES -> NavDestination.Categories
      FAVORITES -> NavDestination.Favorites
      HISTORY -> NavDestination.History
      PROFILE -> NavDestination.Profile
      SETTINGS -> NavDestination.Settings
    }
}