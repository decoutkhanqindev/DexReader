package com.decoutkhanqindev.dexreader.presentation.screens.common.menu

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class MenuItem(
  val id: String,
  val title: String,
  val icon: ImageVector,
)
