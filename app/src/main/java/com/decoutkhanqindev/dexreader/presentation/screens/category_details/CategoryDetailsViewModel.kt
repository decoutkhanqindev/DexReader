package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetMangaListByCategoryUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
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
  private val getMangaListByCategoryUseCase: GetMangaListByCategoryUseCase,
) : ViewModel() {
  private val categoryIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.CategoryDetailsDestination.CATEGORY_ID_ARG])
  val categoryTitleFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.CategoryDetailsDestination.CATEGORY_TITLE_ARG])

  private val _categoryDetailsUiState =
    MutableStateFlow<BasePaginationUiState<Manga>>(BasePaginationUiState.FirstPageLoading)
  val categoryDetailsUiState: StateFlow<BasePaginationUiState<Manga>> =
    _categoryDetailsUiState.asStateFlow()

  private val _categoryCriteriaUiState = MutableStateFlow(CategoryDetailsCriteriaUiState())
  val categoryCriteriaUiState: StateFlow<CategoryDetailsCriteriaUiState> =
    _categoryCriteriaUiState.asStateFlow()

  init {
    fetchMangaListByCategoryFirstPage()
  }

  private fun fetchMangaListByCategoryFirstPage() {
    viewModelScope.launch {
      _categoryDetailsUiState.value = BasePaginationUiState.FirstPageLoading

      val currentCriteriaUiState = _categoryCriteriaUiState.value

      getMangaListByCategoryUseCase(
        categoryId = categoryIdFromArg,
        lastUpdated = currentCriteriaUiState.lastUpdatedOrderId,
        followedCount = currentCriteriaUiState.followedCountOrderId,
        createdAt = currentCriteriaUiState.createdAtOrderId,
        rating = currentCriteriaUiState.ratingOrderId,
        status = currentCriteriaUiState.statusValueIds,
        contentRating = currentCriteriaUiState.contentRatingValueIds
      )
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _categoryDetailsUiState.value = BasePaginationUiState.Content(
            currentList = mangaList,
            currentPage = FIRST_PAGE,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _categoryDetailsUiState.value = BasePaginationUiState.FirstPageError
          Log.d(TAG, "fetchMangaListByCategoryFirstPage have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun fetchMangaListByCategoryNextPage() {
    when (val currentCategoryDetailsUiState = _categoryDetailsUiState.value) {
      BasePaginationUiState.FirstPageLoading,
      BasePaginationUiState.FirstPageError,
        -> return

      is BasePaginationUiState.Content -> {
        when (currentCategoryDetailsUiState.nextPageState) {
          BaseNextPageState.LOADING,
          BaseNextPageState.NO_MORE_ITEMS,
            -> return

          BaseNextPageState.ERROR -> retryFetchMangaListByCategoryNextPage()

          BaseNextPageState.IDLE ->
            fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState)
        }
      }
    }
  }

  private fun fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState: BasePaginationUiState.Content<Manga>) {
    viewModelScope.launch {
      _categoryDetailsUiState.value =
        currentCategoryDetailsUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val currentCriteriaUiState = _categoryCriteriaUiState.value
      val currentMangaList = currentCategoryDetailsUiState.currentList
      val nextPage = currentCategoryDetailsUiState.currentPage + 1

      getMangaListByCategoryUseCase(
        categoryId = categoryIdFromArg,
        offset = currentMangaList.size,
        lastUpdated = currentCriteriaUiState.lastUpdatedOrderId,
        followedCount = currentCriteriaUiState.followedCountOrderId,
        createdAt = currentCriteriaUiState.createdAtOrderId,
        rating = currentCriteriaUiState.ratingOrderId,
        status = currentCriteriaUiState.statusValueIds,
        contentRating = currentCriteriaUiState.contentRatingValueIds
      )
        .onSuccess { nextMangaList ->
          val allMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _categoryDetailsUiState.value = currentCategoryDetailsUiState.copy(
            currentList = allMangaList,
            currentPage = nextPage,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _categoryDetailsUiState.value =
            currentCategoryDetailsUiState.copy(nextPageState = BaseNextPageState.ERROR)
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
    val currentCategoryCriteriaUiState = _categoryCriteriaUiState.value
    if (currentCategoryCriteriaUiState.selectedSortCriteriaId == criteriaId &&
      currentCategoryCriteriaUiState.selectedSortOrderId == orderId
    ) return

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
    contentRatingValueIds: List<String>,
  ) {
    val currentCategoryCriteriaUiState = _categoryCriteriaUiState.value
    if ((currentCategoryCriteriaUiState.statusValueIds == statusValueIds || statusValueIds.isEmpty()) &&
      (currentCategoryCriteriaUiState.contentRatingValueIds == contentRatingValueIds || contentRatingValueIds.isEmpty())
    ) return

    _categoryCriteriaUiState.update {
      it.copy(
        statusValueIds = statusValueIds,
        contentRatingValueIds = contentRatingValueIds
      )
    }

    fetchMangaListByCategoryFirstPage()
  }

  fun retry() {
    if (_categoryDetailsUiState.value is BasePaginationUiState.FirstPageError)
      fetchMangaListByCategoryFirstPage()
  }

  fun retryFetchMangaListByCategoryNextPage() {
    val currentCategoryDetailsUiState = _categoryDetailsUiState.value
    if (currentCategoryDetailsUiState is BasePaginationUiState.Content &&
      currentCategoryDetailsUiState.nextPageState == BaseNextPageState.ERROR
    ) fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState)
  }

  companion object {
    private const val TAG = "CategoryDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}