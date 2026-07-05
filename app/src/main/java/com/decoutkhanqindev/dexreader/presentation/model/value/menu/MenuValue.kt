package com.decoutkhanqindev.dexreader.presentation.model.value.menu

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class MenuValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  HOME(nameRes = R.string.home_menu_item, icon = Icons.Default.Home),
  CATEGORIES(nameRes = R.string.category_menu_item, icon = Icons.Default.Category),
  FAVORITES(nameRes = R.string.favorite_menu_item, icon = Icons.Default.Favorite),
  HISTORY(nameRes = R.string.history_menu_item, icon = Icons.Default.History),
  STATISTICS(nameRes = R.string.statistics_menu_item, icon = Icons.Default.Timeline),
  PROFILE(nameRes = R.string.profile_menu_item, icon = Icons.Default.Person),
  SETTINGS(nameRes = R.string.settings_menu_item, icon = Icons.Default.Settings);

  companion object {
    val bottomBarItems by lazy {
      listOf(HOME, CATEGORIES, FAVORITES, HISTORY, PROFILE)
    }
  }
}
