package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MenuBody(
  items: List<MenuItem>,
  selectedItemId: String,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(items, key = { it.id }) { item ->
      MenuItemRow(
        isSelected = item.id == selectedItemId,
        item = item,
        onItemClick = onItemClick,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}