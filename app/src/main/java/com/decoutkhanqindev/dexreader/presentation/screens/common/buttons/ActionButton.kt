package com.decoutkhanqindev.dexreader.presentation.screens.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ActionButton(
  isEnabled: Boolean = true,
  backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
  elevation: Dp = 8.dp,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  content: @Composable () -> Unit,
) {
  Row(
    modifier = modifier
      .shadow(
        elevation = elevation,
        shape = MaterialTheme.shapes.large,
        clip = false // Don't clip the shadow to allow it to be visible even when the button is disabled
      )
      .alpha(if (isEnabled) 1f else 0.38f)
      .let {
        if (isEnabled) it.onScalableClick(shape = MaterialTheme.shapes.large) { onClick() } else it
      }
      .background(
        color = backgroundColor,
        shape = MaterialTheme.shapes.large
      )
      .padding(horizontal = 16.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier.fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      content()
    }
  }
}

@Preview
@Composable
private fun ActionButtonEnabledPreview() {
  DexReaderTheme {
    ActionButton(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      onClick = {}
    ) {
      Text(text = "Add to Favorites")
    }
  }
}

@Preview
@Composable
private fun ActionButtonDisabledPreview() {
  DexReaderTheme {
    ActionButton(
      isEnabled = false,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      onClick = {}
    ) {
      Text(text = "Add to Favorites")
    }
  }
}