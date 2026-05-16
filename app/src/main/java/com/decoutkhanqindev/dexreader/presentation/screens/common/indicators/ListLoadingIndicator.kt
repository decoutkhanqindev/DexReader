package com.decoutkhanqindev.dexreader.presentation.screens.common.indicators

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ListLoadingIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      LinearProgressIndicator(
        modifier = Modifier.width(125.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )

      Image(
        painter = painterResource(R.drawable.app_icon),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier
          .size(36.dp)
          .shimmer()
      )

      LinearProgressIndicator(
        modifier = Modifier.width(125.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )
    }
  }
}

@Preview
@Composable
private fun ListLoadingIndicatorPreview() {
  DexReaderTheme {
    ListLoadingIndicator(modifier = Modifier.fillMaxWidth())
  }
}