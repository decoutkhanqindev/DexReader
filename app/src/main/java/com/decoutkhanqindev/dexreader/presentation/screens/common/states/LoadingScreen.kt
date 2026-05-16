package com.decoutkhanqindev.dexreader.presentation.screens.common.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(R.drawable.app_icon),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier
          .size(100.dp)
          .shimmer()
      )

      LinearProgressIndicator(
        modifier = Modifier.width(150.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )
    }
  }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
  DexReaderTheme {
    LoadingScreen(modifier = Modifier.fillMaxSize())
  }
}