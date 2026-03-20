package com.decoutkhanqindev.dexreader.presentation.screens.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt

@Composable
fun FavoriteMangaItem(
  manga: FavoriteMangaModel,
  onSelectedManga: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val onClick = remember(manga.id) { { onSelectedManga(manga.id) } }

  Card(
    modifier = modifier,
    onClick = onClick,
    elevation = CardDefaults.cardElevation(8.dp),
    shape = MaterialTheme.shapes.large,
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      MangaCoverArt(
        url = manga.coverUrl,
        title = manga.title,
        modifier = Modifier.fillMaxSize()
      )
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(MaterialTheme.colorScheme.surface.copy(0.8f))
            .padding(horizontal = 4.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
            text = manga.title,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
          )
          Text(
            text = stringResource(R.string.by_author, manga.author),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
          )
          Text(
            text = stringResource(manga.status.nameRes),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
          )
        }
      }
    }
  }
}
