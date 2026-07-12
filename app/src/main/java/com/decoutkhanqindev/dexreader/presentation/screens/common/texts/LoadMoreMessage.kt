package com.decoutkhanqindev.dexreader.presentation.screens.common.texts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoadMoreMessage(
  modifier: Modifier = Modifier,
  autoLoad: Boolean = true,
  onClick: () -> Unit,
) {
  LaunchedEffect(autoLoad) {
    if (autoLoad) {
      onClick()
    }
  }

  Row(
    modifier = modifier
      .clickable { onClick() }
      .padding(horizontal = 24.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
  ) {
    LinearProgressIndicator(
      progress = { 0f },
      modifier = Modifier.width(100.dp),
      color = Color.Transparent,
      trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )

    Text(
      text = stringResource(R.string.load_more),
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
    )

    LinearProgressIndicator(
      progress = { 0f },
      modifier = Modifier.width(100.dp),
      color = Color.Transparent,
      trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )
  }
}

@Preview
@Composable
private fun LoadMoreMessagePreview() {
  DexReaderTheme {
    LoadMoreMessage(
      modifier = Modifier.fillMaxWidth(),
      onClick = {}
    )
  }
}
