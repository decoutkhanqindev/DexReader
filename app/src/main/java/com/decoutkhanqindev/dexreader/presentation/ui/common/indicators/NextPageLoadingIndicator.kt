package com.decoutkhanqindev.dexreader.presentation.ui.common.indicators

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
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
    CircularProgressIndicator(strokeWidth = 6.dp)
  }
}