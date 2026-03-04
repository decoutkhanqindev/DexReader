package com.decoutkhanqindev.dexreader.presentation.screens.search

import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureError

sealed interface SuggestionsUiState {
  data object Loading : SuggestionsUiState
  data class Error(val error: FeatureError = FeatureError.Generic) : SuggestionsUiState
  data object Success : SuggestionsUiState
}