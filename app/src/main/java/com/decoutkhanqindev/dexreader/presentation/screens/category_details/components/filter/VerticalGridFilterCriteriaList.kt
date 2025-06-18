package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.FilterCriteria
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.FilterValue

@Composable
fun VerticalGridFilterCriteriaList(
  selectedStatusValueIds: List<String>,
  onSelectedStatusOptions: (List<String>) -> Unit,
  selectedContentRatingValueIds: List<String>,
  onSelectedContentRatingOptions: (List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  val criteriaList = listOf<FilterCriteria>(
    FilterCriteria.Status,
    FilterCriteria.ContentRating
  )
  val filterStatusOptionList = listOf<FilterValue>(
    FilterValue.Ongoing,
    FilterValue.Completed,
    FilterValue.Hiatus,
    FilterValue.Cancelled
  )
  val filterContentRatingOptionList = listOf<FilterValue>(
    FilterValue.Safe,
    FilterValue.Suggestive,
    FilterValue.Erotica,
  )

  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    items(criteriaList, key = { it.id }) { criteria ->
      FilterCriteriaItem(
        criteria = criteria,
        selectedValueIds =
          if (criteria == FilterCriteria.Status) selectedStatusValueIds
          else selectedContentRatingValueIds,
        filterValueOptions =
          if (criteria == FilterCriteria.Status) filterStatusOptionList
          else filterContentRatingOptionList,
        onSelectedOptions =
          if (criteria == FilterCriteria.Status) onSelectedStatusOptions
          else onSelectedContentRatingOptions,
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      )
    }
  }
}