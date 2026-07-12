package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaDescription(
  description: String,
  modifier: Modifier = Modifier,
) {
  var isExpanded by remember { mutableStateOf(false) }

  Column(modifier = modifier.onScalableClick { isExpanded = !isExpanded }) {
    Text(
      text = description,
      modifier = Modifier.fillMaxWidth(),
      fontWeight = FontWeight.Bold,
      overflow = TextOverflow.Ellipsis,
      maxLines = if (isExpanded) Int.MAX_VALUE else 3,
      style = MaterialTheme.typography.bodyLarge,
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