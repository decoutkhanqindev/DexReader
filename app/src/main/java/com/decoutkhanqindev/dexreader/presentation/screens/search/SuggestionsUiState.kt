package com.decoutkhanqindev.dexreader.presentation.screens.search

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError

@Immutable
sealed interface SuggestionsUiState {
  data object Loading : SuggestionsUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : SuggestionsUiState

  data object Success : SuggestionsUiState
}