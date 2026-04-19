package com.decoutkhanqindev.dexreader.presentation.screens.common.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun IdleScreen(
  message: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(R.drawable.app_icon),
      contentDescription = stringResource(R.string.app_name),
      modifier = Modifier.size(100.dp)
    )

    Text(
      text = message,
      fontWeight = FontWeight.Light,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium,
    )
  }
}

@Preview
@Composable
private fun IdleScreenPreview() {
  DexReaderTheme {
    IdleScreen(
      message = "No mangas found. Try a different search.",
      modifier = Modifier.fillMaxSize()
    )
  }
}
