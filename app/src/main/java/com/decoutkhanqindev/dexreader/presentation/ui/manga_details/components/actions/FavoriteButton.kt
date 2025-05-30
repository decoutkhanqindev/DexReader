package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R

@Composable
fun FavoriteButton(
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  OutlinedButton(
    onClick = { onFavoriteClick },
    shape = MaterialTheme.shapes.large,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = if (isFavorite)
        Color.Red
      else
        MaterialTheme.colorScheme.surface.copy(0.7f)
    ),
    modifier = modifier
  ) {
    Text(
      text = stringResource(R.string.favorite),
      color = MaterialTheme.colorScheme.inverseSurface,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
    )
  }
}