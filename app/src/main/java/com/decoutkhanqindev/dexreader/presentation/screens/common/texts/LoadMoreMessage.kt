package com.decoutkhanqindev.dexreader.presentation.screens.common.texts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.decoutkhanqindev.dexreader.R

@Composable
fun LoadMoreMessage(
  onLoadMore: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.clickable { onLoadMore() },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    HorizontalDivider(modifier = Modifier.weight(1f))
    Text(
      text = stringResource(R.string.load_more),
      style = MaterialTheme.typography.titleMedium,
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
    HorizontalDivider(modifier = Modifier.weight(1f))
  }
}
