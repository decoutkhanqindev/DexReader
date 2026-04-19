package com.decoutkhanqindev.dexreader.presentation.screens.common.texts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme


@Composable
fun LoadPageErrorMessage(
  message: String,
  modifier: Modifier = Modifier,
  onRetryClick: () -> Unit,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = message,
      fontWeight = FontWeight.Light,
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
    )
    IconButton(onClick = onRetryClick) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = stringResource(R.string.retry),
      )
    }
  }
}

@Preview
@Composable
private fun LoadPageErrorMessagePreview() {
  DexReaderTheme {
    LoadPageErrorMessage(
      message = "Can't load next page. Please try again.",
      modifier = Modifier.fillMaxWidth(),
      onRetryClick = {}
    )
  }
}

