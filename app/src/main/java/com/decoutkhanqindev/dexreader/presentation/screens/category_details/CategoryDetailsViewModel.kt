package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
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
        sortCriteria = currentCriteriaUiState.sortCriteria,
        sortOrder = currentCriteriaUiState.sortOrder,
        statusFilter = currentCriteriaUiState.statusFilter,
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter,
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
        sortCriteria = currentCriteriaUiState.sortCriteria,
        sortOrder = currentCriteriaUiState.sortOrder,
        statusFilter = currentCriteriaUiState.statusFilter,
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter,
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
    val newSortCriteria = when (criteriaId) {
      SortCriteria.LatestUpdate.id -> MangaSortCriteria.LATEST_UPDATE
      SortCriteria.Trending.id     -> MangaSortCriteria.TRENDING
      SortCriteria.NewReleases.id  -> MangaSortCriteria.MOST_VIEWED
      SortCriteria.TopRated.id     -> MangaSortCriteria.TOP_RATED
      else                         -> MangaSortCriteria.LATEST_UPDATE
    }
    val newSortOrder = if (orderId == SortOrder.Ascending.id) MangaSortOrder.ASC else MangaSortOrder.DESC

    val current = _categoryCriteriaUiState.value
    if (current.sortCriteria == newSortCriteria && current.sortOrder == newSortOrder) return

    _categoryCriteriaUiState.update {
      it.copy(sortCriteria = newSortCriteria, sortOrder = newSortOrder)
    }
    fetchMangaListByCategoryFirstPage()
  }

  fun updateFilteringCriteria(
    statusValueIds: List<String>,
    contentRatingValueIds: List<String>,
  ) {
    val newStatusFilter = statusValueIds.mapNotNull { id ->
      when (id) {
        FilterValue.Ongoing.id   -> MangaStatusFilter.ON_GOING
        FilterValue.Completed.id -> MangaStatusFilter.COMPLETED
        FilterValue.Hiatus.id    -> MangaStatusFilter.HIATUS
        FilterValue.Cancelled.id -> MangaStatusFilter.CANCELLED
        else                     -> null
      }
    }.ifEmpty { return }

    val newContentRatingFilter = contentRatingValueIds.mapNotNull { id ->
      when (id) {
        FilterValue.Safe.id       -> MangaContentRatingFilter.SAFE
        FilterValue.Suggestive.id -> MangaContentRatingFilter.SUGGESTIVE
        FilterValue.Erotica.id    -> MangaContentRatingFilter.EROTICA
        else                      -> null
      }
    }.ifEmpty { return }

    val current = _categoryCriteriaUiState.value
    if (current.statusFilter == newStatusFilter && current.contentRatingFilter == newContentRatingFilter) return

    _categoryCriteriaUiState.update {
      it.copy(statusFilter = newStatusFilter, contentRatingFilter = newContentRatingFilter)
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
