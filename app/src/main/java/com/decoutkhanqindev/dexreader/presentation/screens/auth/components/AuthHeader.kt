package com.decoutkhanqindev.dexreader.presentation.screens.auth.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun AuthHeader(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = painterResource(R.drawable.app_icon),
      contentDescription = stringResource(R.string.app_name),
      modifier = Modifier
        .size(120.dp)
        .shimmer(true, 1800)
    )

    Text(
      text = stringResource(R.string.app_name),
      color = MaterialTheme.colorScheme.onSurface,
      fontWeight = FontWeight.Black,
      style = MaterialTheme.typography.displaySmall,
      letterSpacing = 2.sp
    )

    Text(
      text = stringResource(R.string.slogan),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = FontWeight.Medium,
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier.padding(top = 4.dp)
    )
  }
}

@Preview
@Composable
private fun AuthHeaderPreview() {
  DexReaderTheme {
    AuthHeader(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    )
  }
}