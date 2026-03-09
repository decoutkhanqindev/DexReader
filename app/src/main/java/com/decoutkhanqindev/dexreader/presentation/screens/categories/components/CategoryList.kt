package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryList(
  items: ImmutableList<CategoryUiModel>,
  onItemClick: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items.forEach {
      CategoryItem(
        item = it,
        onClick = onItemClick,
        modifier = Modifier.wrapContentWidth()
      )
    }
  }
}