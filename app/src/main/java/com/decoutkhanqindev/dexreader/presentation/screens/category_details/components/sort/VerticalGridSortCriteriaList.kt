package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.SortCriteria

@Composable
fun VerticalGridSortCriteriaList(
  selectedCriteriaId: String,
  onSelectedItem: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val criteriaList = listOf<SortCriteria>(
    SortCriteria.LatestUpdate,
    SortCriteria.Trending,
    SortCriteria.NewReleases,
    SortCriteria.TopRated
  )

  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    items(criteriaList, key = { it.id }) { criteria ->
      SortCriteriaItem(
        isSelected = selectedCriteriaId == criteria.id,
        criteria = criteria,
        onSelectedItem = { onSelectedItem(criteria.id) },
        modifier = Modifier
          .padding(4.dp)
          .size(50.dp)
      )
    }
  }
}