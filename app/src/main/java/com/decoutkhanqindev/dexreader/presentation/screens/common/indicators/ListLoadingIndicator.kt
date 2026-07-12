package com.decoutkhanqindev.dexreader.presentation.screens.common.indicators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
      horizontalArrangement = Arrangement.spacedBy(32.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      LinearProgressIndicator(
        modifier = Modifier.weight(1f),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )

      Icon(
        painter = painterResource(R.drawable.app_icon),
        contentDescription = stringResource(R.string.app_name),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
          .size(36.dp)
          .shimmer()
      )

      LinearProgressIndicator(
        modifier = Modifier.weight(1f),
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