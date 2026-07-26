package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.presentation.theme.OnScrim

@Composable
fun MangaInfo(
  title: String,
  author: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = title,
      color = OnScrim,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Start,
      overflow = TextOverflow.Ellipsis,
      maxLines = 2,
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = author,
      color = OnScrim.copy(alpha = 0.8f),
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.labelMedium,
    )
  }
}

@Preview
@Composable
private fun MangaInfoPreview() {
  DexReaderTheme {
    MangaInfo(
      title = "One Piece",
      author = "Eiichiro Oda",
      modifier = Modifier
        .fillMaxWidth()
        .height(85.dp)
    )
  }
}
