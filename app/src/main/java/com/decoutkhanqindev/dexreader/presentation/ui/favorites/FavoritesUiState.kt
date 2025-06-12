package com.decoutkhanqindev.dexreader.presentation.ui.favorites

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga

sealed interface FavoritesUiState {
  data object Idle: FavoritesUiState
  data object FirstPageLoading: FavoritesUiState
  data object FirstPageError: FavoritesUiState
  data class Content(
    val favoriteMangaList: List<FavoriteManga> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: FavoritesNextPageState = FavoritesNextPageState.IDLE
  ) : FavoritesUiState
}

enum class FavoritesNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}
