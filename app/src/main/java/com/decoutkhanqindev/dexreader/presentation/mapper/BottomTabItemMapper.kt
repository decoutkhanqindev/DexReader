package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue.CATEGORIES
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue.HOME
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue.PROFILE
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute

object BottomTabItemMapper {
  fun BottomTabItemValue.toNavRoute() = when (this) {
    HOME -> NavRoute.Home
    CATEGORIES -> NavRoute.Categories
    PROFILE -> NavRoute.Profile
  }
}
