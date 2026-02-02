package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.domain.model.ThemeType

@Immutable
data class ThemeItem(
  val type: ThemeType,
  val name: String,
  val icon: ImageVector,
)
