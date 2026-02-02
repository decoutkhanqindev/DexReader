package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Category

sealed interface CategoriesUiState {
  data object Loading : CategoriesUiState
  data object Error : CategoriesUiState

  @Immutable
  data class Success(
    val genreList: List<Category> = emptyList(),
    val themeList: List<Category> = emptyList(),
    val formatList: List<Category> = emptyList(),
    val contentList: List<Category> = emptyList(),
  ) : CategoriesUiState
}
