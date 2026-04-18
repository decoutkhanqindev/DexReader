package com.decoutkhanqindev.dexreader.presentation.screens.common.menu


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MenuBody(
  selectedItem: MenuValue,
  items: ImmutableList<MenuValue>,
  modifier: Modifier = Modifier,
  onItemClick: (MenuValue) -> Unit,
) {
  LazyColumn(
    modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(items, key = { it }) { item ->
      MenuItemRow(
        isSelected = item == selectedItem,
        item = item,
        modifier = Modifier.fillMaxWidth(),
      ) { onItemClick(it) }
    }
  }
}