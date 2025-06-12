package com.decoutkhanqindev.dexreader.presentation.ui.search

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface SuggestionsUiState {
  data object Loading : SuggestionsUiState
  data object Error : SuggestionsUiState
  data object Success : SuggestionsUiState
}

sealed interface ResultsUiState {
  data object FirstPageLoading : ResultsUiState
  data object FirstPageError : ResultsUiState
  data class Content(
    val mangaList: List<Manga> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: ResultsNextPageState = ResultsNextPageState.IDLE
  ) : ResultsUiState
}

enum class ResultsNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}