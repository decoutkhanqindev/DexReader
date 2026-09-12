package com.decoutkhanqindev.dexreader.presentation.screens.language.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun LanguageItem(
  language: LanguageValue,
  displayName: String,
  isSelected: Boolean,
  modifier: Modifier = Modifier.Companion,
  onClick: () -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme

  Row(
    modifier = modifier
      .onClick(shape = MaterialTheme.shapes.medium, action = onClick)
      .background(
        color = if (isSelected) colorScheme.primaryContainer.copy(alpha = 0.9f)
        else colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
      )
      .padding(horizontal = 16.dp, vertical = 18.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = language.flag,
      style = MaterialTheme.typography.titleLarge,
    )

    Text(
      text = displayName,
      modifier = Modifier.weight(1f),
      color = colorScheme.onSurface,
      fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
      style = MaterialTheme.typography.titleMedium,
    )

    if (isSelected) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = colorScheme.primary,
      )
    }
  }
}

@Preview
@Composable
private fun LanguageItemPreview() {
  DexReaderTheme {
    LanguageItem(
      language = LanguageValue.ENGLISH,
      displayName = "English",
      isSelected = true,
      onClick = {},
    )
  }
}
