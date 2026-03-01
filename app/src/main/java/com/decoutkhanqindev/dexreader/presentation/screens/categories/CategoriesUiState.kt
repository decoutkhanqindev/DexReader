package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeOption

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data object Error : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: Map<CategoryTypeOption, List<Category>> = emptyMap(),
  ) : CategoriesUiState
}
