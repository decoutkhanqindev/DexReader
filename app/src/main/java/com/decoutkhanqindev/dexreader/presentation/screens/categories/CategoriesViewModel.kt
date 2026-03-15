package com.decoutkhanqindev.dexreader.presentation.screens.categories


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.category.CategoryType
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetCategoryListUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryUiMapper.toCategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.UiErrorMapper.toFeatureUiError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentList
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
                  ?: persistentListOf()).map { it.toCategoryUiModel() }.toPersistentList()
              }
              .toImmutableMap()
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
