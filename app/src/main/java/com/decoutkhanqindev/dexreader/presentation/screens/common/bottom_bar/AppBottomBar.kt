package com.decoutkhanqindev.dexreader.presentation.screens.common.bottom_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun AppBottomBar(
  selectedItem: BottomTabItemValue,
  modifier: Modifier = Modifier,
  onItemClick: (BottomTabItemValue) -> Unit,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    BottomTabItemValue.entries.forEach { item ->
      AppBottomBarItem(
        item = item,
        isSelected = item == selectedItem,
        modifier = Modifier.weight(1f),
        onClick = onItemClick,
      )
    }
  }
}

@Composable
private fun AppBottomBarItem(
  item: BottomTabItemValue,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: (BottomTabItemValue) -> Unit,
) {
  val onItemClick = remember(item) { { onClick(item) } }
  val contentColor = if (isSelected) MaterialTheme.colorScheme.primary
  else MaterialTheme.colorScheme.onSurfaceVariant

  Column(
    modifier = modifier
      .onClick(shape = MaterialTheme.shapes.medium, action = onItemClick)
      .padding(vertical = 6.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    Icon(
      imageVector = item.icon,
      contentDescription = null,
      tint = contentColor,
      modifier = Modifier.size(24.dp)
    )
    Text(
      text = stringResource(item.nameRes),
      color = contentColor,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      style = MaterialTheme.typography.labelMedium,
    )
  }
}

@Preview
@Composable
private fun AppBottomBarHomePreview() {
  DexReaderTheme {
    AppBottomBar(
      selectedItem = BottomTabItemValue.HOME,
      modifier = Modifier.fillMaxWidth(),
      onItemClick = {}
    )
  }
}

@Preview
@Composable
private fun AppBottomBarProfilePreview() {
  DexReaderTheme {
    AppBottomBar(
      selectedItem = BottomTabItemValue.PROFILE,
      modifier = Modifier.fillMaxWidth(),
      onItemClick = {}
    )
  }
}
