package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeOption
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureError

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data class Error(val error: FeatureError = FeatureError.Generic) : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: Map<CategoryTypeOption, List<Category>> = emptyMap(),
  ) : CategoriesUiState
}
