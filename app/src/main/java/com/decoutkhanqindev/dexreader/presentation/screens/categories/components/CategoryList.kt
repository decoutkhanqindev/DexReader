package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryList(
  items: ImmutableList<CategoryModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String, String) -> Unit,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items.forEach {
      CategoryItem(
        item = it,
        modifier = Modifier.wrapContentWidth(),
      ) { id, name -> onItemClick(id, name) }
    }
  }
}