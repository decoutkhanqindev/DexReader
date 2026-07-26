package com.decoutkhanqindev.dexreader.presentation.screens.common.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaStatusBadge(
  status: MangaStatusValue,
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
  contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
  Surface(
    modifier = modifier,
    color = containerColor,
    shape = MaterialTheme.shapes.small,
    tonalElevation = 4.dp,
    shadowElevation = 4.dp
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = status.icon,
        contentDescription = null,
        tint = contentColor,
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = stringResource(status.nameRes).uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Black,
        color = contentColor
      )
    }
  }
}

@Preview
@Composable
private fun MangaStatusBadgePreview() {
  DexReaderTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      MangaStatusValue.entries.forEach { status -> MangaStatusBadge(status = status) }
    }
  }
}
