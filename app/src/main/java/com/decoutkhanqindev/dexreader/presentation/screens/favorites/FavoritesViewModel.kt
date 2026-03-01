package com.decoutkhanqindev.dexreader.presentation.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.FavoritesHistoryException
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.ObserveFavoritesUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
  private val observeFavoritesUseCase: ObserveFavoritesUseCase,
) : ViewModel() {
  private val _uiState =
    MutableStateFlow<BasePaginationUiState<FavoriteManga>>(BasePaginationUiState.FirstPageLoading)
  val uiState: StateFlow<BasePaginationUiState<FavoriteManga>> = _uiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  private var observeFavoritesJob: Job? = null

  init {
    observeFavoritesFirstPage()
  }

  private fun observeFavoritesFirstPage() {
    cancelObserveFavoritesJob()
    observeFavoritesJob = viewModelScope.launch {
      _uiState.value = BasePaginationUiState.FirstPageLoading

      _userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = BasePaginationUiState.FirstPageLoading
          cancelObserveFavoritesJob()
          return@collectLatest
        }

        try {
          observeFavoritesUseCase(
            userId = userId,
            limit = MANGA_LIST_PER_PAGE_SIZE,
            lastFavoriteMangaId = null
          ).collect { result ->
            result
              .onSuccess { favoriteMangaList ->
                val hasNextPage = favoriteMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
                _uiState.value = BasePaginationUiState.Content(
                  currentList = favoriteMangaList,
                  currentPage = FIRST_PAGE,
                  nextPageState =
                    if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
                    else BaseNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable is FavoritesHistoryException.PermissionDenied && _userId.value == null) {
                  _uiState.value = BasePaginationUiState.FirstPageLoading
                  return@onFailure
                }

                _uiState.value = BasePaginationUiState.FirstPageError
                Log.d(
                  TAG,
                  "observeFavoritesFirstPage have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e is FavoritesHistoryException.PermissionDenied && _userId.value == null)
            _uiState.value = BasePaginationUiState.FirstPageLoading
          else {
            _uiState.value = BasePaginationUiState.FirstPageError
            Log.d(TAG, "observeFavoritesFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun observeFavoritesNextPage() {
    when (val currentUiState = _uiState.value) {
      BasePaginationUiState.FirstPageError,
      BasePaginationUiState.FirstPageLoading,
        -> return

      is BasePaginationUiState.Content -> {
        when (currentUiState.nextPageState) {
          BaseNextPageState.LOADING,
          BaseNextPageState.NO_MORE_ITEMS,
            -> return

          BaseNextPageState.ERROR -> retryObserveFavoritesNextPage()
          BaseNextPageState.IDLE -> observeFavoritesNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun observeFavoritesNextPageInternal(currentUiState: BasePaginationUiState.Content<FavoriteManga>) {
    observeFavoritesJob = viewModelScope.launch {
      _uiState.value = currentUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val favoriteMangaList = currentUiState.currentList
      val lastFavoriteMangaId = favoriteMangaList.lastOrNull()?.id
      val nextPage = currentUiState.currentPage + 1

      _userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = BasePaginationUiState.FirstPageLoading
          cancelObserveFavoritesJob()
          return@collectLatest
        }

        try {
          observeFavoritesUseCase(
            userId = userId,
            limit = MANGA_LIST_PER_PAGE_SIZE,
            lastFavoriteMangaId = lastFavoriteMangaId
          ).collect { result ->
            result
              .onSuccess { nextPageFavoriteMangaList ->
                val allFavoriteMangaList = favoriteMangaList + nextPageFavoriteMangaList
                val hasNextPage = nextPageFavoriteMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
                _uiState.value = currentUiState.copy(
                  currentList = allFavoriteMangaList,
                  currentPage = nextPage,
                  nextPageState =
                    if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
                    else BaseNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable is FavoritesHistoryException.PermissionDenied && _userId.value == null)
                  return@onFailure

                _uiState.value = currentUiState.copy(nextPageState = BaseNextPageState.ERROR)
                Log.d(
                  TAG,
                  "observeFavoritesNextPageInternal have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e is FavoritesHistoryException.PermissionDenied && _userId.value == null)
            return@collectLatest
          else {
            _uiState.value = currentUiState.copy(nextPageState = BaseNextPageState.ERROR)
            Log.d(
              TAG,
              "observeFavoritesNextPageInternal setup error: ${e.stackTraceToString()}"
            )
          }
        }
      }
    }
  }

  fun updateUserId(userId: String?) {
    if (_userId.value == userId) return
    _userId.value = userId
  }

  fun retry() {
    if (_uiState.value is BasePaginationUiState.FirstPageError)
      observeFavoritesFirstPage()
  }

  fun retryObserveFavoritesNextPage() {
    val currentUiState = _uiState.value
    if (currentUiState is BasePaginationUiState.Content &&
      currentUiState.nextPageState == BaseNextPageState.ERROR
    ) observeFavoritesNextPageInternal(currentUiState)
  }

  private fun cancelObserveFavoritesJob() {
    observeFavoritesJob?.cancel()
    observeFavoritesJob = null
  }

  override fun onCleared() {
    cancelObserveFavoritesJob()
    super.onCleared()
  }

  companion object {
    private const val TAG = "FavoritesViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}