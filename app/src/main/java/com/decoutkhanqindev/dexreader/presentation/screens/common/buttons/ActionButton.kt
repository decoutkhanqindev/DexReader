package com.decoutkhanqindev.dexreader.presentation.screens.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick

@Composable
fun ActionButton(
  isEnabled: Boolean = true,
  backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  content: @Composable RowScope.() -> Unit,
) {
  val shape = MaterialTheme.shapes.medium

  Row(
    modifier = modifier
      .alpha(if (isEnabled) 1f else 0.38f)
      .let {
        if (isEnabled) it.onScalableClick(shape = shape) { onClick() } else it
      }
      .background(
        color = backgroundColor,
        shape = shape
      )
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = shape
      )
      .padding(horizontal = 16.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) { content() }
}