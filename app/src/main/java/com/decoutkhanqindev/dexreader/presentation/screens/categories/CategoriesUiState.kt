package com.decoutkhanqindev.dexreader.presentation.screens.categories


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryTypeUiModel
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: ImmutableMap<CategoryTypeUiModel, ImmutableList<CategoryUiModel>> = persistentMapOf(),
  ) : CategoriesUiState
}
