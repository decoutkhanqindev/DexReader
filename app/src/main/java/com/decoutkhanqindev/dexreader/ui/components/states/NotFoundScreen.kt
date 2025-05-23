package com.decoutkhanqindev.dexreader.ui.components.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.decoutkhanqindev.dexreader.ui.theme.DexReaderTheme

@Composable
fun NotFoundScreen(
  message: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(R.drawable.not_found),
      contentDescription = stringResource(R.string.not_found),
      modifier = Modifier.size(80.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = message,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Light,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun NotFoundScreenPreview() {
  DexReaderTheme {
    NotFoundScreen(
      message = stringResource(R.string.sorry_no_manga_found_with_title, "One Piece"),
      modifier = Modifier.fillMaxSize()
    )
  }
}