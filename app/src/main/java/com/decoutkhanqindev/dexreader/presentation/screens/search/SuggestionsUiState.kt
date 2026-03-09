package com.decoutkhanqindev.dexreader.presentation.screens.search

import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError

sealed interface SuggestionsUiState {
  data object Loading : SuggestionsUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : SuggestionsUiState
  data object Success : SuggestionsUiState
}