package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryDetailsCriteriaUiState(
  val selectedSortCriteriaId: String = SortCriteria.LatestUpdate.id,
  val selectedSortOrderId: String = SortOrder.Descending.id,
  val lastUpdatedOrderId: String? = null,
  val followedCountOrderId: String? = null,
  val createdAtOrderId: String? = null,
  val ratingOrderId: String? = null,
  val statusValueIds: List<String> = listOf(FilterValue.Ongoing.id),
  val contentRatingValueIds: List<String> = listOf(FilterValue.Safe.id),
)
