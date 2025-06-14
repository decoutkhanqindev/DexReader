package com.decoutkhanqindev.dexreader.presentation.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.ObserveFavoritesUseCase
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
  private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Idle)
  val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  private var observeFavoritesJob: Job? = null

  init {
    observeFavoritesFirstPage()
  }

  private fun observeFavoritesFirstPage() {
    cancelObserveFavoritesJob()
    observeFavoritesJob = viewModelScope.launch {
      _uiState.value = FavoritesUiState.FirstPageLoading

      _userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = FavoritesUiState.Idle
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
                _uiState.value = FavoritesUiState.Content(
                  favoriteMangaList = favoriteMangaList,
                  currentPage = FIRST_PAGE,
                  nextPageState =
                    if (!hasNextPage) FavoritesNextPageState.NO_MORE_ITEMS
                    else FavoritesNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
                  _uiState.value = FavoritesUiState.Idle
                  return@onFailure
                }

                _uiState.value = FavoritesUiState.FirstPageError
                Log.d(
                  TAG,
                  "observeFavoritesFirstPage have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
            _uiState.value = FavoritesUiState.Idle
          } else {
            _uiState.value = FavoritesUiState.FirstPageError
            Log.d(TAG, "observeFavoritesFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun observeFavoritesNextPage() {
    when (val currentUiState = _uiState.value) {
      FavoritesUiState.Idle,
      FavoritesUiState.FirstPageError,
      FavoritesUiState.FirstPageLoading
        -> return

      is FavoritesUiState.Content -> {
        when (currentUiState.nextPageState) {
          FavoritesNextPageState.LOADING,
          FavoritesNextPageState.NO_MORE_ITEMS
            -> return

          FavoritesNextPageState.ERROR -> retryObserveFavoritesNextPage()
          FavoritesNextPageState.IDLE -> observeFavoritesNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun observeFavoritesNextPageInternal(currentUiState: FavoritesUiState.Content) {
    observeFavoritesJob = viewModelScope.launch {
      _uiState.value = currentUiState.copy(nextPageState = FavoritesNextPageState.LOADING)

      val favoriteMangaList = currentUiState.favoriteMangaList
      val lastFavoriteMangaId = favoriteMangaList.lastOrNull()?.id
      val nextPage = currentUiState.currentPage + 1

      _userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = FavoritesUiState.Idle
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
                  favoriteMangaList = allFavoriteMangaList,
                  currentPage = nextPage,
                  nextPageState =
                    if (!hasNextPage) FavoritesNextPageState.NO_MORE_ITEMS
                    else FavoritesNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
                  return@onFailure
                }

                _uiState.value = currentUiState.copy(nextPageState = FavoritesNextPageState.ERROR)
                Log.d(
                  TAG,
                  "observeFavoritesNextPageInternal have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
            return@collectLatest
          } else {
            _uiState.value = currentUiState.copy(nextPageState = FavoritesNextPageState.ERROR)
            Log.d(
              TAG,
              "observeFavoritesNextPageInternal setup error: ${e.stackTraceToString()}"
            )
          }
        }
      }
    }
  }

  fun updateUserId(userId: String) {
    if (_userId.value == userId) return
    _userId.value = userId
  }

  fun retry() {
    if (_uiState.value is FavoritesUiState.FirstPageError)
      observeFavoritesFirstPage()
  }

  fun retryObserveFavoritesNextPage() {
    val currentUiState = _uiState.value
    if (currentUiState is FavoritesUiState.Content &&
      currentUiState.nextPageState == FavoritesNextPageState.ERROR
    ) observeFavoritesNextPageInternal(currentUiState)
  }

  fun reset() {
    cancelObserveFavoritesJob()
    _uiState.value = FavoritesUiState.Idle
    _userId.value = null
  }

  private fun cancelObserveFavoritesJob() {
    observeFavoritesJob?.cancel()
    observeFavoritesJob = null
  }

  override fun onCleared() {
    super.onCleared()
    cancelObserveFavoritesJob()
  }

  companion object {
    private const val TAG = "FavoritesViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
    private const val PERMISSION_DENIED_EXCEPTION = "PERMISSION_DENIED"
  }
}