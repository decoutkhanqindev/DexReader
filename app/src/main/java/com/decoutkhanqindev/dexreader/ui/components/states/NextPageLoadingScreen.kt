package com.decoutkhanqindev.dexreader.ui.components.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.ui.theme.DexReaderTheme

@Composable
fun NextPageLoadingScreen(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.BottomCenter
  ) {
    CircularProgressIndicator(
      strokeWidth = 6.dp,
      modifier = Modifier.padding(bottom = 16.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun NextPageLoadingScreenPreview() {
  DexReaderTheme {
    NextPageLoadingScreen(modifier = Modifier.fillMaxSize())
  }
}
