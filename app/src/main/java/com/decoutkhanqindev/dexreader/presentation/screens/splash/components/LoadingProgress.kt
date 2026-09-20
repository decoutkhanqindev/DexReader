package com.decoutkhanqindev.dexreader.presentation.screens.splash.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun LoadingProgress(
  progress: () -> Float,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = stringResource(R.string.loading),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      )
      Text(
        text = "${(progress() * 100).toInt()}%",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      )
    }

    LinearProgressIndicator(
      progress = progress,
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
      strokeCap = StrokeCap.Round,
      gapSize = 0.dp,
      drawStopIndicator = {},
    )
  }
}

@Preview
@Composable
private fun LoadingProgressPreview() {
  DexReaderTheme {
    LoadingProgress(
      progress = { 0.4f },
      modifier = Modifier.fillMaxWidth(),
    )
  }
}
