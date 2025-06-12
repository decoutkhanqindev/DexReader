package com.decoutkhanqindev.dexreader.presentation.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.favorite.ObserveFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
  private val observeFavoritesUseCase: ObserveFavoritesUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.FirstPageLoading)
  val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)
  val userId: StateFlow<String?> = _userId.asStateFlow()

  init {
    observeFavoriteMangaListFirstPage()
  }

  private fun observeFavoriteMangaListFirstPage() {
    viewModelScope.launch {
      _uiState.value = FavoritesUiState.FirstPageLoading

      userId.collect {
        it?.let { userId ->
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
              .onFailure {
                _uiState.value = FavoritesUiState.FirstPageError
                Log.d(
                  TAG,
                  "observeFavoriteMangaListFirstPage have error: ${it.stackTraceToString()}"
                )
              }
          }
        }
      }
    }
  }

  fun observeFavoriteMangaListNextPage() {
    when (val currentUiState = _uiState.value) {
      FavoritesUiState.FirstPageError,
      FavoritesUiState.FirstPageLoading
        -> return

      is FavoritesUiState.Content -> {
        when (currentUiState.nextPageState) {
          FavoritesNextPageState.LOADING,
          FavoritesNextPageState.NO_MORE_ITEMS
            -> return

          FavoritesNextPageState.ERROR -> retryObserveFavoriteMangaListNextPage()
          FavoritesNextPageState.IDLE -> observeFavoriteMangaListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun observeFavoriteMangaListNextPageInternal(currentUiState: FavoritesUiState.Content) {
    viewModelScope.launch {
      _uiState.value = currentUiState.copy(nextPageState = FavoritesNextPageState.LOADING)

      val favoriteMangaList = currentUiState.favoriteMangaList
      val lastFavoriteMangaId = favoriteMangaList.last().id
      val nextPage = currentUiState.currentPage + 1

      userId.collect {
        it?.let { userId ->
          observeFavoritesUseCase(
            userId = userId,
            limit = favoriteMangaList.size,
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
              .onFailure {
                _uiState.value = currentUiState.copy(nextPageState = FavoritesNextPageState.ERROR)
                Log.d(
                  TAG,
                  "observeFavoriteMangaListNextPageInternal have error: ${it.stackTraceToString()}"
                )
              }
          }
        }
      }
    }
  }

  fun updateUserId(userId: String) {
    _userId.value = userId
  }

  fun retry() {
    observeFavoriteMangaListFirstPage()
  }

  fun retryObserveFavoriteMangaListNextPage() {
    val currentUiState = _uiState.value
    if (currentUiState is FavoritesUiState.Content &&
      currentUiState.nextPageState == FavoritesNextPageState.ERROR
    ) observeFavoriteMangaListNextPage()
  }

  companion object {
    private const val TAG = "FavoritesViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}