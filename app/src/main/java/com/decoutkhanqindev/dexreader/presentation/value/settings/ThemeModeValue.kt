package com.decoutkhanqindev.dexreader.presentation.value.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

enum class ThemeModeValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  LIGHT(
    nameRes = R.string.light,
    icon = Icons.Default.LightMode
  ),
  DARK(
    nameRes = R.string.dark,
    icon = Icons.Default.DarkMode
  ),
  SYSTEM(
    nameRes = R.string.system,
    icon = Icons.Default.PhoneAndroid
  )
}
