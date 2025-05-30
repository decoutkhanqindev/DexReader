package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonsSection(
  isReading: Boolean,
  onReadingClick: (String) -> Unit,
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    ReadingButton(
      isReading = isReading,
      onReadingClick = onReadingClick,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
    FavoriteButton(
      isFavorite = isFavorite,
      onFavoriteClick = onFavoriteClick,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
  }
}