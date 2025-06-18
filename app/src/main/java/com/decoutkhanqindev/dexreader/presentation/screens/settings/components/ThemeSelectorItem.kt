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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.ThemeType

@Composable
fun ThemeSelectorItem(
  isSelected: Boolean,
  theme: ThemeItem,
  onSelectedTheme: (ThemeType) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.clickable { onSelectedTheme(theme.type) },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(
      selected = isSelected,
      onClick = { onSelectedTheme(theme.type) },
    )
    Text(
      text = theme.name,
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(end = 4.dp)
    )
    Icon(
      imageVector = theme.icon,
      contentDescription = theme.name,
      modifier = Modifier.size(24.dp)
    )
  }
}