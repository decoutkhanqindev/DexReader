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
  enabled: Boolean = true,
  title: String,
  onSubmitClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    enabled = enabled,
    shape = MaterialTheme.shapes.medium,
    onClick = onSubmitClick,
    modifier = modifier.height(48.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center
    )
  }
}