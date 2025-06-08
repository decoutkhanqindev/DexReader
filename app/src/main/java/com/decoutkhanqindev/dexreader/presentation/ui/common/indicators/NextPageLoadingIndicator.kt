package com.decoutkhanqindev.dexreader.presentation.ui.common.indicators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NextPageLoadingIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.BottomCenter
  ) {
    LinearProgressIndicator(
      modifier = Modifier
        .width(100.dp)
        .padding(top = 8.dp),
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )
  }
}