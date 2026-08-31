package com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class BottomTabItemValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  HOME(nameRes = R.string.home_menu_item, icon = Icons.Default.Home),
  CATEGORIES(nameRes = R.string.category_menu_item, icon = Icons.Default.Category),
  PROFILE(nameRes = R.string.profile_menu_item, icon = Icons.Default.Person),
}
