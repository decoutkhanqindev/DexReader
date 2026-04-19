package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SwipeToDismissBackground(
  isSwiping: Boolean,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .background(
        if (isSwiping) Color.Red
        else Color.Transparent
      ),
    contentAlignment = Alignment.CenterEnd
  ) {
    Icon(
      imageVector = Icons.Default.Delete,
      contentDescription = null,
      tint = Color.White,
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp)
    )
  }
}

@Preview
@Composable
private fun SwipeToDismissBackgroundSwipingPreview() {
  DexReaderTheme {
    SwipeToDismissBackground(
      isSwiping = true,
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
    )
  }
}

@Preview
@Composable
private fun SwipeToDismissBackgroundIdlePreview() {
  DexReaderTheme {
    SwipeToDismissBackground(
      isSwiping = false,
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
    )
  }
}