package com.decoutkhanqindev.dexreader.presentation.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetCategoryListUseCase
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
        .onSuccess {
          val genreList = it.filter { it.group == CategoryGroup.Genre.id }
          val themeList = it.filter { it.group == CategoryGroup.Theme.id }
          val formatList = it.filter { it.group == CategoryGroup.Format.id }
          val contentList = it.filter { it.group == CategoryGroup.Content.id }
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
