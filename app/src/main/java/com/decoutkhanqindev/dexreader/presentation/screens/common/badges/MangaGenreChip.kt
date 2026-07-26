package com.decoutkhanqindev.dexreader.presentation.screens.common.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaGenreChip(
  label: String,
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
  contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  onClick: (() -> Unit)? = null,
) {
  Surface(
    modifier = if (onClick != null) {
      modifier.onClick(shape = MaterialTheme.shapes.small) { onClick() }
    } else {
      modifier
    },
    color = containerColor,
    contentColor = contentColor,
    shape = MaterialTheme.shapes.small,
    tonalElevation = 4.dp,
    shadowElevation = 4.dp,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Black,
      color = contentColor,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Preview
@Composable
private fun MangaGenreChipPreview() {
  DexReaderTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      MangaGenreChip(label = "Action")
      MangaGenreChip(label = "Slice of Life", onClick = {})
    }
  }
}
