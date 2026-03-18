package com.decoutkhanqindev.dexreader.presentation.screens.categories


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryTypeModel
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data class Error(val error: FeatureError = FeatureError.Generic) : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: ImmutableMap<CategoryTypeModel, ImmutableList<CategoryModel>> = persistentMapOf(),
  ) : CategoriesUiState
}
