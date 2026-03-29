package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info

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
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel

@Composable
fun MangaInfo(
  manga: MangaModel,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = manga.title,
      modifier = Modifier.padding(bottom = 4.dp),
      fontWeight = FontWeight.ExtraBold,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleLarge,
    )
    Text(
      text = stringResource(R.string.author, manga.author),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.artist, manga.artist),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.year, manga.year),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.status, stringResource(manga.status.nameRes)),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.content_rating, stringResource(manga.contentRating.nameRes)),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}