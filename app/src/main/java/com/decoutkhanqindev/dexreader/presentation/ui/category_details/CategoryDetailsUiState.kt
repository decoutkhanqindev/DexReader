package com.decoutkhanqindev.dexreader.presentation.ui.category_details

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface CategoryDetailsUiState {
  data object FirstPageLoading : CategoryDetailsUiState
  data object FirstPageError : CategoryDetailsUiState
  data class Content(
    val mangaList: List<Manga> = emptyList(),
    val currentPage: Int = 1,
    val nextPageState: CategoryDetailsNextPageState = CategoryDetailsNextPageState.IDLE,
  ) : CategoryDetailsUiState
}

enum class CategoryDetailsNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}

data class CategoryDetailsCriteriaUiState(
  val lastUpdatedOrderId: String? = SortOrder.Descending.id,
  val followedCountOrderId: String? = null,
  val createdAtOrderId : String? = null,
  val ratingOrderId: String? = null,
  val statusValueId: String = FilterValue.Ongoing.id,
  val contentRatingValueId: String = FilterValue.Safe.id,
)
