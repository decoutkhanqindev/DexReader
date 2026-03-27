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
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MenuBody(
  items: ImmutableList<MenuItemValue>,
  selectedItem: MenuItemValue,
  modifier: Modifier = Modifier,
  onItemClick: (MenuItemValue) -> Unit,
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
        onClick = onItemClick
      )
    }
  }
}