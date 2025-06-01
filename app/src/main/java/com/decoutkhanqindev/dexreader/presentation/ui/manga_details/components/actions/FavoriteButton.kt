package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun FavoriteButton(
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    onClick = { onFavoriteClick },
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surface.copy(0.5f)
    ),
    modifier = modifier.border(
      width = 1.dp,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      shape = MaterialTheme.shapes.medium
    )
  ) {
    Text(
      text = if (isFavorite) stringResource(R.string.unfavorite) else stringResource(R.string.favorite),
      color = MaterialTheme.colorScheme.inverseSurface,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
    )
  }
}