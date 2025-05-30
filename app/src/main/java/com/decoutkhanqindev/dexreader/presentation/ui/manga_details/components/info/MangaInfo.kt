package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga
import java.util.Locale

@Composable
fun MangaInfo(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = manga.title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.author, manga.author),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.artist, manga.artist),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.year, manga.year),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.status, manga.status.capitalize(Locale.US)),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
  }
}