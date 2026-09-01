package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.SettingItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick

@Composable
fun SettingRow(
  item: SettingItemValue,
  modifier: Modifier = Modifier.Companion,
  onClick: (() -> Unit)? = null,
  trailing: @Composable () -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme
  val clickModifier = if (onClick != null) {
    Modifier.onClick(shape = MaterialTheme.shapes.medium, action = onClick)
  } else Modifier.Companion

  Row(
    modifier = modifier
      .then(clickModifier)
      .background(color = colorScheme.surfaceContainer, shape = MaterialTheme.shapes.medium)
      .padding(horizontal = 16.dp, vertical = 18.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = item.icon,
      contentDescription = null,
      tint = colorScheme.primary,
      modifier = Modifier.size(24.dp)
    )

    Text(
      text = stringResource(item.nameRes),
      modifier = Modifier.weight(1f),
      color = colorScheme.onSurface,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.titleMedium,
    )

    trailing()
  }
}