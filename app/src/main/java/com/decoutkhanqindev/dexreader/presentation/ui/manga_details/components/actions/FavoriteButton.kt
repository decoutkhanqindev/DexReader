package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton

@Composable
fun FavoriteButton(
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionButton(
    onClick = { onFavoriteClick },
    content = {
      Text(
        text = if (isFavorite) stringResource(R.string.unfavorite) else stringResource(R.string.favorite),
        color = MaterialTheme.colorScheme.inverseSurface,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
      )
    },
    modifier = modifier
  )
}