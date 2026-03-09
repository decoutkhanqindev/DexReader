package com.decoutkhanqindev.dexreader.presentation.screens.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.CategoryType
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetCategoryListUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryUiMapper.toCategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureUiError
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
  private val getCategoryListUseCase: GetCategoryListUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
  val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

  init {
    fetchTagList()
  }

  private fun fetchTagList() {
    viewModelScope.launch {
      _uiState.value = CategoriesUiState.Loading

      getCategoryListUseCase()
        .onSuccess { grouped ->
          val categoryMap =
            CategoryTypeUiModel.entries
              .filter { it != CategoryTypeUiModel.UNKNOWN }
              .associateWith { type ->
                (grouped[CategoryType.valueOf(type.name)]
                  ?: emptyList()).map { it.toCategoryUiModel() }
              }
          _uiState.value = CategoriesUiState.Success(categoryMap = categoryMap)
        }
        .onFailure { throwable ->
          _uiState.value = CategoriesUiState.Error(throwable.toFeatureUiError())
          Log.e(TAG, "fetchTagList have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun retry() {
    if (_uiState.value is CategoriesUiState.Error) fetchTagList()
  }

  companion object {
    private const val TAG = "CategoriesViewModel"
  }
}
