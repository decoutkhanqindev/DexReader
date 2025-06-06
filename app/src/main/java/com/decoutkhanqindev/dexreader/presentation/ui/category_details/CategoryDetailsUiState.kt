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

data class CategoryDetailsCriteriaState(
  val lastUpdated: String = SortingCriteria.Descending.value,
  val followedCount: String = SortingCriteria.Descending.value,
  val createdAt: String = SortingCriteria.Descending.value,
  val rating: String = SortingCriteria.Descending.value,
  val status: String = FilteringCriteria.Ongoing.value,
  val contentRating: String = FilteringCriteria.Safe.value,
)
