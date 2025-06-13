package com.decoutkhanqindev.dexreader.presentation.ui.history

import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory

sealed interface HistoryUiState {
  data object Idle : HistoryUiState
  data object FirstPageLoading : HistoryUiState
  data object FirstPageError : HistoryUiState
  data class Content(
    val readingHistoryList: List<ReadingHistory> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: HistoryNextPageState = HistoryNextPageState.IDLE
  ) : HistoryUiState
}

enum class HistoryNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}