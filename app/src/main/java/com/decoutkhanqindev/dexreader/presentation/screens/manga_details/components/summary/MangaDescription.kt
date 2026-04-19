package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaDescription(
  description: String,
  modifier: Modifier = Modifier,
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier.clickable { isExpanded = !isExpanded }) {
    Text(
      text = description,
      modifier = Modifier.fillMaxWidth(),
      fontWeight = FontWeight.Bold,
      overflow = TextOverflow.Ellipsis,
      maxLines = if (isExpanded) Int.MAX_VALUE else 3,
      style = MaterialTheme.typography.bodyLarge,
    )

    Icon(
      imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
      contentDescription =
        if (isExpanded) stringResource(R.string.icon_expand_less)
        else stringResource(R.string.icon_expand_more),
      modifier = Modifier.align(Alignment.End)
    )
  }
}

@Preview
@Composable
private fun MangaDescriptionCollapsedPreview() {
  DexReaderTheme {
    MangaDescription(
      description = "Monkey D. Luffy sets off on an adventure to find the legendary treasure known as the One Piece and become the Pirate King. Along the way he recruits a crew of diverse and powerful companions.",
      modifier = Modifier.fillMaxWidth()
    )
  }
}