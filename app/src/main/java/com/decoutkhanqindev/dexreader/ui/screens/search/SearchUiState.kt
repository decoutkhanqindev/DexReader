package com.decoutkhanqindev.dexreader.ui.screens.search

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface SearchSuggestionsUiState {
  data object Loading : SearchSuggestionsUiState
  data object Error : SearchSuggestionsUiState
  data object Success : SearchSuggestionsUiState
}

sealed interface SearchResultsUiState {
  data object FirstPageLoading : SearchResultsUiState
  data object FirstPageError : SearchResultsUiState
  data class Content(
    val items: List<Manga> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: SearchMangaListNextPageState = SearchMangaListNextPageState.IDLE
  ) : SearchResultsUiState
}

enum class SearchMangaListNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}