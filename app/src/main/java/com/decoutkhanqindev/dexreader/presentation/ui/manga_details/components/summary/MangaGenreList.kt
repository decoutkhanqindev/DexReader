package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MangaGenreList(
  genreList: List<String>,
  onSelectedGenre: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(genreList, key = { it }) { genre ->
        MangaGenreItem(
          genre = genre,
          onSelectedGenre = onSelectedGenre,
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
        )
      }
    }
  }
}