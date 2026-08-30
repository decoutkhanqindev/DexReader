package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ThemeOptionItem(
  item: ThemeModeValue,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: (ThemeModeValue) -> Unit,
) {
  val onItemClick = remember(item) { { onClick(item) } }

  Row(
    modifier = modifier
      .onClick(shape = MaterialTheme.shapes.medium, action = onItemClick)
      .padding(vertical = 8.dp, horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      imageVector = item.icon,
      contentDescription = null,
      tint = if (isSelected) MaterialTheme.colorScheme.primary
      else MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(24.dp)
    )
    Text(
      text = stringResource(item.nameRes),
      fontWeight = FontWeight.Bold,
      color = if (isSelected) MaterialTheme.colorScheme.primary
      else MaterialTheme.colorScheme.onSurfaceVariant,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview
@Composable
private fun ThemeOptionItemSelectedPreview() {
  DexReaderTheme {
    ThemeOptionItem(
      item = ThemeModeValue.SYSTEM,
      isSelected = true,
      modifier = Modifier.width(110.dp),
      onClick = {},
    )
  }
}

@Preview
@Composable
private fun ThemeOptionItemUnselectedPreview() {
  DexReaderTheme {
    ThemeOptionItem(
      item = ThemeModeValue.DARK,
      isSelected = false,
      modifier = Modifier.width(110.dp),
      onClick = {},
    )
  }
}
