package com.decoutkhanqindev.dexreader.ui.screens.search

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface SearchMangaSuggestionsUiState {
  data object Loading : SearchMangaSuggestionsUiState
  data object Error : SearchMangaSuggestionsUiState
  data object Success : SearchMangaSuggestionsUiState
}

sealed interface SearchMangaResultsUiState {
  data object FirstPageLoading : SearchMangaResultsUiState
  data object FirstPageError : SearchMangaResultsUiState
  data class Content(
    val mangaList: List<Manga> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: SearchMangaResultsNextPageState = SearchMangaResultsNextPageState.IDLE
  ) : SearchMangaResultsUiState
}

enum class SearchMangaResultsNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}