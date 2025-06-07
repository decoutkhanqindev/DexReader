package com.decoutkhanqindev.dexreader.presentation.ui.category_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetMangaListByCategoryUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
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
  private val getMangaListByCategoryUseCase: GetMangaListByCategoryUseCase
) : ViewModel() {
  private val categoryIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.CategoryDetailsDestination.CATEGORY_ID_ARG])
  val categoryTitleFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.CategoryDetailsDestination.CATEGORY_TITLE_ARG])

  private val _categoryDetailsUiState =
    MutableStateFlow<CategoryDetailsUiState>(CategoryDetailsUiState.FirstPageLoading)
  val categoryDetailsUiState: StateFlow<CategoryDetailsUiState> =
    _categoryDetailsUiState.asStateFlow()

  private val _categoryCriteriaUiState = MutableStateFlow(CategoryDetailsCriteriaUiState())
  val categoryCriteriaUiState: StateFlow<CategoryDetailsCriteriaUiState> =
    _categoryCriteriaUiState.asStateFlow()

  init {
    fetchMangaListByCategoryFirstPage()
  }

  private fun fetchMangaListByCategoryFirstPage() {
    viewModelScope.launch {
      _categoryDetailsUiState.value = CategoryDetailsUiState.FirstPageLoading

      val currentCriteria = _categoryCriteriaUiState.value

      val mangaListResult = getMangaListByCategoryUseCase(
        categoryId = categoryIdFromArg,
        lastUpdated = currentCriteria.lastUpdatedOrderId,
        followedCount = currentCriteria.followedCountOrderId,
        createdAt = currentCriteria.createdAtOrderId,
        rating = currentCriteria.ratingOrderId,
        status = currentCriteria.statusValueIds,
        contentRating = currentCriteria.contentRatingValueIds
      )
      mangaListResult
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _categoryDetailsUiState.value = CategoryDetailsUiState.Content(
            mangaList = mangaList,
            currentPage = FIRST_PAGE,
            nextPageState = if (!hasNextPage) CategoryDetailsNextPageState.NO_MORE_ITEMS
            else CategoryDetailsNextPageState.IDLE
          )
        }
        .onFailure {
          _categoryDetailsUiState.value = CategoryDetailsUiState.FirstPageError
          Log.d(TAG, "fetchMangaListByCategoryFirstPage have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun fetchMangaListByCategoryNextPage() {
    when (val currentUiState = _categoryDetailsUiState.value) {
      CategoryDetailsUiState.FirstPageLoading,
      CategoryDetailsUiState.FirstPageError -> return

      is CategoryDetailsUiState.Content -> {
        when (currentUiState.nextPageState) {
          CategoryDetailsNextPageState.LOADING,
          CategoryDetailsNextPageState.NO_MORE_ITEMS -> return

          CategoryDetailsNextPageState.ERROR -> retryFetchMangaListByCategoryNextPage()

          CategoryDetailsNextPageState.IDLE -> fetchMangaListByCategoryNextPageInternal(
            currentUiState
          )
        }
      }
    }
  }

  private fun fetchMangaListByCategoryNextPageInternal(currentUiState: CategoryDetailsUiState.Content) {
    viewModelScope.launch {
      _categoryDetailsUiState.value =
        currentUiState.copy(nextPageState = CategoryDetailsNextPageState.LOADING)

      val currentCriteria = _categoryCriteriaUiState.value
      val currentMangaList = currentUiState.mangaList
      val nextPage = currentUiState.currentPage + 1

      val nextMangaListResults = getMangaListByCategoryUseCase(
        categoryId = categoryIdFromArg,
        offset = currentMangaList.size,
        lastUpdated = currentCriteria.lastUpdatedOrderId,
        followedCount = currentCriteria.followedCountOrderId,
        createdAt = currentCriteria.createdAtOrderId,
        rating = currentCriteria.ratingOrderId,
        status = currentCriteria.statusValueIds,
        contentRating = currentCriteria.contentRatingValueIds
      )
      nextMangaListResults
        .onSuccess { nextMangaList ->
          val updatedMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _categoryDetailsUiState.value = currentUiState.copy(
            mangaList = updatedMangaList,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage) CategoryDetailsNextPageState.NO_MORE_ITEMS
            else CategoryDetailsNextPageState.IDLE
          )
        }
        .onFailure {
          _categoryDetailsUiState.value =
            currentUiState.copy(nextPageState = CategoryDetailsNextPageState.ERROR)
          Log.d(
            TAG,
            "fetchMangaListByCategoryNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }


  fun updateSortingCriteria(
    criteriaId: String,
    orderId: String,
  ) {
    _categoryCriteriaUiState.update {
      it.copy(
        selectedSortCriteriaId = criteriaId,
        selectedSortOrderId = orderId,
        lastUpdatedOrderId = null,
        followedCountOrderId = null,
        createdAtOrderId = null,
        ratingOrderId = null
      )
    }

    when (criteriaId) {
      SortCriteria.LatestUpdate.id -> _categoryCriteriaUiState.update { it.copy(lastUpdatedOrderId = orderId) }
      SortCriteria.Trending.id -> _categoryCriteriaUiState.update { it.copy(followedCountOrderId = orderId) }
      SortCriteria.NewReleases.id -> _categoryCriteriaUiState.update { it.copy(createdAtOrderId = orderId) }
      SortCriteria.TopRated.id -> _categoryCriteriaUiState.update { it.copy(ratingOrderId = orderId) }
    }

    fetchMangaListByCategoryFirstPage()
  }

  fun updateFilteringCriteria(
    statusValueIds: List<String>,
    contentRatingValueIds: List<String>
  ) {
    _categoryCriteriaUiState.update {
      it.copy(
        statusValueIds = statusValueIds,
        contentRatingValueIds = contentRatingValueIds
      )
    }
    fetchMangaListByCategoryFirstPage()
  }

  fun retry() {
    fetchMangaListByCategoryFirstPage()
  }

  fun retryFetchMangaListByCategoryNextPage() {
    val currentUiState = _categoryDetailsUiState.value
    if (currentUiState is CategoryDetailsUiState.Content &&
      currentUiState.nextPageState == CategoryDetailsNextPageState.ERROR
    ) {
      fetchMangaListByCategoryNextPageInternal(currentUiState)
    }
  }

  companion object {
    private const val TAG = "CategoryDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}