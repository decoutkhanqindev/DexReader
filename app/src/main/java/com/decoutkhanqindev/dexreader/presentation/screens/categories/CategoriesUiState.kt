package com.decoutkhanqindev.dexreader.presentation.screens.categories


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

@Immutable
sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : CategoriesUiState

  @Immutable
  data class Success(
    val categoryMap: ImmutableMap<CategoryTypeValue, ImmutableList<CategoryModel>> = persistentMapOf(),
  ) : CategoriesUiState
}
