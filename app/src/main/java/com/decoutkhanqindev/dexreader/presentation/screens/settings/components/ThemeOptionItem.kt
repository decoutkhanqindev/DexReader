package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue

@Composable
fun ThemeOptionItem(
  isSelected: Boolean,
  item: ThemeModeValue,
  modifier: Modifier = Modifier,
  onClick: (ThemeModeValue) -> Unit,
) {
  Row(
    modifier = modifier.clickable { onClick(item) },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(
      selected = isSelected,
      onClick = { onClick(item) },
    )
    Text(
      text = stringResource(item.nameRes),
      modifier = Modifier.padding(end = 4.dp),
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Icon(imageVector = item.icon, contentDescription = item.name, modifier = Modifier.size(24.dp))
  }
}

@Preview(showBackground = true)
@Composable
private fun ThemeOptionItemSelectedPreview() {
  ThemeOptionItem(
    isSelected = true,
    item = ThemeModeValue.SYSTEM,
    onClick = {},
  )
}

@Preview(showBackground = true)
@Composable
private fun ThemeOptionItemUnselectedPreview() {
  ThemeOptionItem(
    isSelected = false,
    item = ThemeModeValue.LIGHT,
    onClick = {},
  )
}
