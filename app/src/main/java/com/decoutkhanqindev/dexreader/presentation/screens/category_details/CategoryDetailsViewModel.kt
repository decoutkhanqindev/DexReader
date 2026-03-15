package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetMangaListByCategoryUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaContentRatingFilter
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaSortCriteria
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaStatusFilter
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaUiMapper.toMangaUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.UiErrorMapper.toFeatureUiError
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaUiModel
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
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
    MutableStateFlow<BasePaginationUiState<MangaUiModel>>(BasePaginationUiState.FirstPageLoading)
  val categoryDetailsUiState: StateFlow<BasePaginationUiState<MangaUiModel>> =
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
        sortCriteria = currentCriteriaUiState.sortCriteria.toMangaSortCriteria(),
        sortOrder = currentCriteriaUiState.sortOrder.toMangaSortOrder(),
        statusFilter = currentCriteriaUiState.statusFilter.map { it.toMangaStatusFilter() },
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter.map { it.toMangaContentRatingFilter() },
      )
        .onSuccess { mangaList ->
          _categoryDetailsUiState.value = BasePaginationUiState.Content(
            currentList = mangaList.map { it.toMangaUiModel() }.toPersistentList(),
            currentPage = FIRST_PAGE,
            nextPageState = BaseNextPageState.fromPageSize(mangaList.size, MANGA_LIST_PER_PAGE_SIZE)
          )
        }
        .onFailure { throwable ->
          _categoryDetailsUiState.value =
            BasePaginationUiState.FirstPageError(throwable.toFeatureUiError())
          Log.d(
            TAG,
            "fetchMangaListByCategoryFirstPage have error: ${throwable.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchMangaListByCategoryNextPage() {
    when (val currentCategoryDetailsUiState = _categoryDetailsUiState.value) {
      BasePaginationUiState.FirstPageLoading,
      is BasePaginationUiState.FirstPageError,
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

  private fun fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState: BasePaginationUiState.Content<MangaUiModel>) {
    viewModelScope.launch {
      _categoryDetailsUiState.value =
        currentCategoryDetailsUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val currentCriteriaUiState = _categoryCriteriaUiState.value
      val currentMangaList = currentCategoryDetailsUiState.currentList
      val nextPage = currentCategoryDetailsUiState.currentPage + 1

      getMangaListByCategoryUseCase(
        categoryId = categoryIdFromArg,
        offset = currentMangaList.size,
        sortCriteria = currentCriteriaUiState.sortCriteria.toMangaSortCriteria(),
        sortOrder = currentCriteriaUiState.sortOrder.toMangaSortOrder(),
        statusFilter = currentCriteriaUiState.statusFilter.map { it.toMangaStatusFilter() },
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter.map { it.toMangaContentRatingFilter() },
      )
        .onSuccess { nextMangaList ->
          val allMangaList =
            (currentMangaList + nextMangaList.map { it.toMangaUiModel() }).toPersistentList()
          _categoryDetailsUiState.value = currentCategoryDetailsUiState.copy(
            currentList = allMangaList,
            currentPage = nextPage,
            nextPageState = BaseNextPageState.fromPageSize(
              nextMangaList.size,
              MANGA_LIST_PER_PAGE_SIZE
            )
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
    sortCriteria: MangaSortCriteriaUiModel,
    sortOrder: MangaSortOrderUiModel,
  ) {
    val current = _categoryCriteriaUiState.value
    if (current.sortCriteria == sortCriteria && current.sortOrder == sortOrder) return

    _categoryCriteriaUiState.update {
      it.copy(sortCriteria = sortCriteria, sortOrder = sortOrder)
    }
    fetchMangaListByCategoryFirstPage()
  }

  fun updateFilteringCriteria(
    statusFilter: ImmutableList<MangaStatusFilterUiModel>,
    contentRatingFilter: ImmutableList<MangaContentRatingFilterUiModel>,
  ) {
    val current = _categoryCriteriaUiState.value
    if (current.statusFilter == statusFilter && current.contentRatingFilter == contentRatingFilter) return

    _categoryCriteriaUiState.update {
      it.copy(statusFilter = statusFilter, contentRatingFilter = contentRatingFilter)
    }
    fetchMangaListByCategoryFirstPage()
  }

  fun retry() {
    if (_categoryDetailsUiState.value is BasePaginationUiState.FirstPageError)
      fetchMangaListByCategoryFirstPage()
  }

  fun retryFetchMangaListByCategoryNextPage() {
    val currentCategoryDetailsUiState = _categoryDetailsUiState.value
    if (currentCategoryDetailsUiState is BasePaginationUiState.Content<MangaUiModel> &&
      currentCategoryDetailsUiState.nextPageState == BaseNextPageState.ERROR
    ) fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState)
  }

  companion object {
    private const val TAG = "CategoryDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}
