package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.compose.runtime.Immutable

sealed interface BasePaginationUiState<out T> {
  data object FirstPageLoading : BasePaginationUiState<Nothing>
  data object FirstPageError : BasePaginationUiState<Nothing>

  @Immutable
  data class Content<T>(
    val currentList: List<T> = emptyList(),
    val currentPage: Int = 1,
    val nextPageState: BaseNextPageState = BaseNextPageState.IDLE,
  ) : BasePaginationUiState<T>
}

enum class BaseNextPageState {
  IDLE,
  LOADING,
  NO_MORE_ITEMS,
  ERROR
}
