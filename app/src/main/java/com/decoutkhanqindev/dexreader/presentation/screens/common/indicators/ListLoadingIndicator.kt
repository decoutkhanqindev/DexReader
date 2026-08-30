package com.decoutkhanqindev.dexreader.presentation.screens.common.indicators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ListLoadingIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
      LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(0.4f),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )
  }
}

@Preview
@Composable
private fun ListLoadingIndicatorPreview() {
  DexReaderTheme {
    ListLoadingIndicator(modifier = Modifier.fillMaxWidth())
  }
}