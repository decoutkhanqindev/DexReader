package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun ReadingButton(
  isReading: Boolean,
  onReadingClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    onClick = { onReadingClick },
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surface.copy(0.5f)
    ),
    modifier = modifier.border(
      width = 1.dp,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      shape = MaterialTheme.shapes.medium
    )
  ) {
    Text(
      text = if (isReading)
        stringResource(R.string.continue_reading)
      else
        stringResource(R.string.start_reading),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.inverseSurface,
      fontWeight = FontWeight.ExtraBold,
    )
  }
}