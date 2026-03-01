package com.decoutkhanqindev.dexreader.presentation.screens.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.CategoryType
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        .onSuccess { categoryList ->
          var genreList: List<Category> = emptyList()
          var themeList: List<Category> = emptyList()
          var formatList: List<Category> = emptyList()
          var contentList: List<Category> = emptyList()

          // Switch to Default dispatcher for CPU-intensive filtering operations
          withContext(Dispatchers.Default) {
            genreList = categoryList.filter { it.type == CategoryType.GENRE }
            themeList = categoryList.filter { it.type == CategoryType.THEME }
            formatList = categoryList.filter { it.type == CategoryType.FORMAT }
            contentList = categoryList.filter { it.type == CategoryType.CONTENT }
          }

          _uiState.value = CategoriesUiState.Success(
            genreList = genreList,
            themeList = themeList,
            formatList = formatList,
            contentList = contentList
          )
        }
        .onFailure {
          _uiState.value = CategoriesUiState.Error
          Log.e(TAG, "fetchTagList have error: ${it.stackTraceToString()}")
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
