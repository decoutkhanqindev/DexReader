package com.decoutkhanqindev.dexreader.presentation.screens.search

sealed interface SuggestionsUiState {
  data object Loading : SuggestionsUiState
  data object Error : SuggestionsUiState
  data object Success : SuggestionsUiState
}