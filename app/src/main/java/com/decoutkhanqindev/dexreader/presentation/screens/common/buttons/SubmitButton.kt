package com.decoutkhanqindev.dexreader.presentation.screens.common.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(
  title: String,
  isEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier.height(48.dp),
    enabled = isEnabled,
    shape = MaterialTheme.shapes.medium,
  ) {
    Text(
      text = title,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
    )
  }
}