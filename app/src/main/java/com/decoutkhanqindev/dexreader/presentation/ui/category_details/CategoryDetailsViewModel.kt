package com.decoutkhanqindev.dexreader.presentation.ui.category_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetMangaListByCategoryUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMangaListByTagUseCase: GetMangaListByCategoryUseCase
) : ViewModel() {
  private val categoryId: String =
    checkNotNull(savedStateHandle[NavigationDestination.CategoryDetailsScreen.TAG_ID_ARG])

  private val _uiState =
    MutableStateFlow<CategoryDetailsUiState>(CategoryDetailsUiState.FirstPageLoading)
  val uiState: StateFlow<CategoryDetailsUiState> = _uiState.asStateFlow()

  private val _criteriaState = MutableStateFlow(CategoryDetailsCriteriaState())
  val criteriaState: StateFlow<CategoryDetailsCriteriaState> = _criteriaState.asStateFlow()

  init {
    fetchMangaListByTagFirstPage()
  }

  private fun fetchMangaListByTagFirstPage() {
    viewModelScope.launch {
      _uiState.value = CategoryDetailsUiState.FirstPageLoading

      val currentCriteria = _criteriaState.value

      val mangaListResult = getMangaListByTagUseCase(
        categoryId = categoryId,
        lastUpdated = currentCriteria.lastUpdated,
        followedCount = currentCriteria.followedCount,
        createdAt = currentCriteria.createdAt,
        rating = currentCriteria.rating,
        status = currentCriteria.status,
        contentRating = currentCriteria.contentRating
      )
      mangaListResult
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _uiState.value = CategoryDetailsUiState.Content(
            mangaList = mangaList,
            currentPage = FIRST_PAGE,
            nextPageState = if (!hasNextPage) CategoryDetailsNextPageState.NO_MORE_ITEMS
            else CategoryDetailsNextPageState.IDLE
          )
        }
        .onFailure {
          _uiState.value = CategoryDetailsUiState.FirstPageError
          Log.d(TAG, "fetchMangaListByTagFirstPage have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun fetchMangaListByTagNextPage() {
    when (val currentUiState = _uiState.value) {
      CategoryDetailsUiState.FirstPageLoading,
      CategoryDetailsUiState.FirstPageError -> return

      is CategoryDetailsUiState.Content -> {
        when (currentUiState.nextPageState) {
          CategoryDetailsNextPageState.LOADING,
          CategoryDetailsNextPageState.NO_MORE_ITEMS -> return

          CategoryDetailsNextPageState.ERROR -> retryFetchMangaListByTagNextPage()

          CategoryDetailsNextPageState.IDLE -> fetchMangaListByTagNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun fetchMangaListByTagNextPageInternal(currentUiState: CategoryDetailsUiState.Content) {
    viewModelScope.launch {
      _uiState.value = currentUiState.copy(nextPageState = CategoryDetailsNextPageState.LOADING)

      val currentCriteria = _criteriaState.value
      val currentMangaList = currentUiState.mangaList
      val nextPage = currentUiState.currentPage + 1

      val nextMangaListResults = getMangaListByTagUseCase(
        categoryId = categoryId,
        offset = currentMangaList.size,
        lastUpdated = currentCriteria.lastUpdated,
        followedCount = currentCriteria.followedCount,
        createdAt = currentCriteria.createdAt,
        rating = currentCriteria.rating,
        status = currentCriteria.status,
        contentRating = currentCriteria.contentRating
      )
      nextMangaListResults
        .onSuccess { nextMangaList ->
          val updatedMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _uiState.value = currentUiState.copy(
            mangaList = updatedMangaList,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage) CategoryDetailsNextPageState.NO_MORE_ITEMS
            else CategoryDetailsNextPageState.IDLE
          )
        }
        .onFailure {
          _uiState.value =
            currentUiState.copy(nextPageState = CategoryDetailsNextPageState.ERROR)
          Log.d(TAG, "fetchMangaListByTagNextPageInternal have error: ${it.stackTraceToString()}")
        }
    }
  }


  fun updateSortingCriteria(
    lastUpdated: String = _criteriaState.value.lastUpdated,
    followedCount: String = _criteriaState.value.followedCount,
    createdAt: String = _criteriaState.value.createdAt,
    rating: String = _criteriaState.value.rating
  ) {
    _criteriaState.update {
      it.copy(
        lastUpdated = lastUpdated,
        followedCount = followedCount,
        createdAt = createdAt,
        rating = rating
      )
    }
    fetchMangaListByTagFirstPage()
  }

  fun updateFilteringCriteria(
    status: String = _criteriaState.value.status,
    contentRating: String = _criteriaState.value.contentRating
  ) {
    _criteriaState.update {
      it.copy(
        status = status,
        contentRating = contentRating
      )
    }
    fetchMangaListByTagFirstPage()
  }

  fun retry() {
    fetchMangaListByTagFirstPage()
  }

  fun retryFetchMangaListByTagNextPage() {
    val currentUiState = _uiState.value
    if (currentUiState is CategoryDetailsUiState.Content &&
      currentUiState.nextPageState == CategoryDetailsNextPageState.ERROR
    ) {
      fetchMangaListByTagNextPageInternal(currentUiState)
    }
  }

  companion object {
    private const val TAG = "CategoryDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}