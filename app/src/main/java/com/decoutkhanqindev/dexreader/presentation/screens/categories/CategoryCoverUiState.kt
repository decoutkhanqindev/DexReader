package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError

@Immutable
sealed interface CategoryCoverUiState {
  data object Loading : CategoryCoverUiState

  @Immutable
  data class Success(val coverUrl: String = "") : CategoryCoverUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : CategoryCoverUiState
}
