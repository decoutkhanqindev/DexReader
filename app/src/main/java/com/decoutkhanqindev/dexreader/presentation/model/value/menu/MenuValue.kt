package com.decoutkhanqindev.dexreader.presentation.model.value.menu

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class MenuValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
  val isDrawerItem: Boolean = true,
) {
  HOME(nameRes = R.string.home_menu_item, icon = Icons.Default.Home),
  CATEGORIES(nameRes = R.string.category_menu_item, icon = Icons.Default.Category),
  FAVORITES(
    nameRes = R.string.favorite_menu_item,
    icon = Icons.Default.Favorite,
    isDrawerItem = false
  ),
  HISTORY(
    nameRes = R.string.history_menu_item,
    icon = Icons.Default.History,
    isDrawerItem = false
  ),
  STATISTICS(
    nameRes = R.string.statistics_menu_item,
    icon = Icons.Default.Timeline,
    isDrawerItem = false
  ),
  PROFILE(nameRes = R.string.profile_menu_item, icon = Icons.Default.Person);

  companion object {
    val drawerItems: List<MenuValue> = entries.filter { it.isDrawerItem }
  }
}
