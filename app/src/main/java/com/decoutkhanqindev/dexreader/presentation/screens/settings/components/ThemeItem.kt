package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.domain.model.ThemeType

data class ThemeItem(
  val type: ThemeType,
  val name: String,
  val icon: ImageVector
)
