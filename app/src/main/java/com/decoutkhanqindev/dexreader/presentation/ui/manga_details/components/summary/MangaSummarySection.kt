package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Manga

@Composable
fun MangaSummarySection(
  manga: Manga,
  onSelectedGenre: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    MangaSummaryTitle(
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .padding(bottom = 12.dp)
    )
    MangaDescription(
      description = manga.description,
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp),
    )
    MangaGenreList(
      manga = manga,
      onSelectedGenre = onSelectedGenre,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
