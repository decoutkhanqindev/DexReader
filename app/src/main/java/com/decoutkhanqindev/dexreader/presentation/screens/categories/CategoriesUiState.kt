package com.decoutkhanqindev.dexreader.presentation.screens.categories


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeUiModel
import com.decoutkhanqindev.dexreader.presentation.model.CategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError
import kotlinx.collections.immutable.ImmutableList

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: Map<CategoryTypeUiModel, ImmutableList<CategoryUiModel>> = emptyMap(),
  ) : CategoriesUiState
}
