package com.decoutkhanqindev.dexreader.presentation.screens.statistics.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun StatCard(label: String, millis: Long) {
  val minutes = (millis / 60_000).toInt()
  val hours = minutes / 60
  val remainingMinutes = minutes % 60

  Card(
    modifier = Modifier.padding(8.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = label, style = MaterialTheme.typography.titleMedium)
      Text(
        text = if (hours > 0) {
          stringResource(
            R.string.hours_suffix,
            hours
          ) + " " + stringResource(R.string.minutes_suffix, remainingMinutes)
        } else {
          stringResource(R.string.minutes_suffix, minutes)
        },
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
      )
    }
  }
}

@Preview
@Composable
private fun StatCardPreview() {
  DexReaderTheme {
    StatCard(label = "Daily Reading Time", millis = 5_400_000L)
  }
}
