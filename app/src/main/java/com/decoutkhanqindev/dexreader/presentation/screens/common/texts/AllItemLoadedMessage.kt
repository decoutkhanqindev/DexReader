package com.decoutkhanqindev.dexreader.presentation.screens.common.texts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AllItemLoadedMessage(
  title: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    HorizontalDivider(modifier = Modifier.weight(0.8f))
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .weight(1.4f)
        .fillMaxWidth()
    )
    HorizontalDivider(modifier = Modifier.weight(0.8f))
  }
}