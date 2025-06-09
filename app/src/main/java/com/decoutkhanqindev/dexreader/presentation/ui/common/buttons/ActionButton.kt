package com.decoutkhanqindev.dexreader.presentation.ui.common.buttons

import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
  enabled: Boolean = true,
  onClick: () -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surface.copy(0.2f)
    ),
    modifier = modifier.border(
      width = 1.dp,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      shape = MaterialTheme.shapes.medium
    )
  ) { content() }
}