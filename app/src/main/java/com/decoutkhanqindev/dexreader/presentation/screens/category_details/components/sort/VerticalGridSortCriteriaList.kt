package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue

@Composable
fun VerticalGridSortCriteriaList(
  selectedItem: MangaSortCriteriaValue,
  onItemClick: (MangaSortCriteriaValue) -> Unit,
  modifier: Modifier = Modifier,
) {
  val lazyGridState = rememberLazyGridState()

  LazyVerticalGrid(
    state = lazyGridState,
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    items(MangaSortCriteriaValue.entries, key = { it.name }) { criteria ->
      SortCriteriaItem(
        isSelected = selectedItem == criteria,
        item = criteria,
        onClick = { onItemClick(criteria) },
        modifier = Modifier
          .padding(4.dp)
          .size(50.dp)
      )
    }
  }
}
