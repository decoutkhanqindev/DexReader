package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaOption

@Composable
fun VerticalGridSortCriteriaList(
  selectedCriteria: MangaSortCriteriaOption,
  onSelectedItem: (MangaSortCriteriaOption) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    items(MangaSortCriteriaOption.entries, key = { it.name }) { criteria ->
      SortCriteriaItem(
        isSelected = selectedCriteria == criteria,
        criteria = criteria,
        onSelectedItem = { onSelectedItem(criteria) },
        modifier = Modifier
          .padding(4.dp)
          .size(50.dp)
      )
    }
  }
}
