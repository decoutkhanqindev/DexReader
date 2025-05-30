package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MangaGenreItem(
  genre: String,
  onSelectedGenre: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(4.dp),
    onClick = { onSelectedGenre(genre) }) {
    Text(
      text = genre,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
    )
  }
}