package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R

@Composable
fun ReadingButton(
  isReading: Boolean,
  onReadingClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  OutlinedButton(
    onClick = { onReadingClick },
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surface.copy(0.7f)
    ),
    modifier = modifier
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