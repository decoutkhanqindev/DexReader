package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton

@Composable
fun ReadingButton(
  canRead: Boolean,
  isReading: Boolean,
  onReadingClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionButton(
    enabled = canRead,
    onClick = onReadingClick,
    content = {
      Text(
        text = if (isReading)
          stringResource(R.string.continue_reading)
        else
          stringResource(R.string.start_reading),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.inverseSurface,
        fontWeight = FontWeight.ExtraBold,
      )
    },
    modifier = modifier
  )
}