package com.decoutkhanqindev.dexreader.presentation.value.menu

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute

enum class MenuItemValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  HOME(nameRes = R.string.home_menu_item, icon = Icons.Default.Home),
  CATEGORIES(nameRes = R.string.category_menu_item, icon = Icons.Default.Category),
  FAVORITES(nameRes = R.string.favorite_menu_item, icon = Icons.Default.Favorite),
  HISTORY(nameRes = R.string.history_menu_item, icon = Icons.Default.History),
  PROFILE(nameRes = R.string.profile_menu_item, icon = Icons.Default.Person),
  SETTINGS(nameRes = R.string.settings_menu_item, icon = Icons.Default.Settings);

  companion object {
    fun MenuItemValue.toNavRoute() = when (this) {
      HOME -> NavRoute.Home
      CATEGORIES -> NavRoute.Categories
      FAVORITES -> NavRoute.Favorites
      HISTORY -> NavRoute.History
      PROFILE -> NavRoute.Profile
      SETTINGS -> NavRoute.Settings
    }
  }
}
