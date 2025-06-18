package com.decoutkhanqindev.dexreader.presentation.ui.category_details

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
