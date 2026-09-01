package com.decoutkhanqindev.dexreader.presentation.model.value.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class SettingItemValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  THEME(
    nameRes = R.string.dark_mode,
    icon = Icons.Default.DarkMode
  ),
  LANGUAGE(
    nameRes = R.string.language,
    icon = Icons.Default.Language
  ),
  PRIVACY(
    nameRes = R.string.privacy_policy,
    icon = Icons.Default.PrivacyTip
  ),
}
