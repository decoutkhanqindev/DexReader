package com.decoutkhanqindev.dexreader.presentation.ui.common.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun ErrorScreen(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.Warning,
      contentDescription = null,
      modifier = Modifier
        .size(80.dp)
        .padding(bottom = 8.dp)
    )
    Text(
      text = message,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Light,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(
          start = 8.dp,
          bottom = 8.dp
        )
    )
    Button(
      onClick = onRetry,
      shape = MaterialTheme.shapes.large
    ) {
      Text(
        text = stringResource(R.string.retry),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(4.dp)
      )
    }
  }
}
