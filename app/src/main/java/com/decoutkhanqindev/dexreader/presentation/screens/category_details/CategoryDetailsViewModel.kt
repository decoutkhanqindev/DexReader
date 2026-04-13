package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import timber.log.Timber
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetMangaListByCategoryUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaSortCriteria
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toMangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureError
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaContentRating
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaModel
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaStatus
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortOrderValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
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
  private val route: NavRoute.CategoryDetails = savedStateHandle.toRoute()
  private val categoryIdFromArg: String = route.categoryId
  val categoryTitleFromArg: String = route.categoryTitle

  private val _categoryDetailsUiState =
    MutableStateFlow<BasePaginationUiState<MangaModel>>(BasePaginationUiState.FirstPageLoading)
  val categoryDetailsUiState: StateFlow<BasePaginationUiState<MangaModel>> =
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
        statusFilter = currentCriteriaUiState.statusFilter.map { it.toMangaStatus() },
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter.map { it.toMangaContentRating() },
      )
        .onSuccess { mangaList ->
          _categoryDetailsUiState.value = BasePaginationUiState.Content(
            currentList = mangaList.map { it.toMangaModel() }.toPersistentList(),
            currentPage = FIRST_PAGE,
            nextPageState = BaseNextPageState.fromPageSize(
              resultSize = mangaList.size,
              pageSize = MANGA_LIST_PER_PAGE_SIZE
            )
          )
        }
        .onFailure { throwable ->
          _categoryDetailsUiState.value =
            BasePaginationUiState.FirstPageError(throwable.toFeatureError())
          Timber.tag(this::class.java.simpleName).d("fetchMangaListByCategoryFirstPage have error: ${throwable.stackTraceToString()}")
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

  private fun fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState: BasePaginationUiState.Content<MangaModel>) {
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
        statusFilter = currentCriteriaUiState.statusFilter.map { it.toMangaStatus() },
        contentRatingFilter = currentCriteriaUiState.contentRatingFilter.map { it.toMangaContentRating() },
      )
        .onSuccess { nextMangaList ->
          val allMangaList =
            (currentMangaList + nextMangaList.map { it.toMangaModel() }).toPersistentList()
          _categoryDetailsUiState.value = currentCategoryDetailsUiState.copy(
            currentList = allMangaList,
            currentPage = nextPage,
            nextPageState = BaseNextPageState.fromPageSize(
              resultSize = nextMangaList.size,
              pageSize = MANGA_LIST_PER_PAGE_SIZE
            )
          )
        }
        .onFailure {
          _categoryDetailsUiState.value =
            currentCategoryDetailsUiState.copy(nextPageState = BaseNextPageState.ERROR)
          Timber.tag(this::class.java.simpleName).d("fetchMangaListByCategoryNextPageInternal have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun updateSortingCriteria(
    sortCriteria: MangaSortCriteriaValue,
    sortOrder: MangaSortOrderValue,
  ) {
    val current = _categoryCriteriaUiState.value
    if (current.sortCriteria == sortCriteria && current.sortOrder == sortOrder) return

    _categoryCriteriaUiState.update {
      it.copy(
        sortCriteria = sortCriteria,
        sortOrder = sortOrder
      )
    }
    fetchMangaListByCategoryFirstPage()
  }

  fun updateFilteringCriteria(
    statusFilter: ImmutableList<MangaStatusValue>,
    contentRatingFilter: ImmutableList<MangaContentRatingValue>,
  ) {
    val current = _categoryCriteriaUiState.value
    if (current.statusFilter == statusFilter && current.contentRatingFilter == contentRatingFilter) return

    _categoryCriteriaUiState.update {
      it.copy(
        statusFilter = statusFilter,
        contentRatingFilter = contentRatingFilter
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
    if (currentCategoryDetailsUiState is BasePaginationUiState.Content<MangaModel> &&
      currentCategoryDetailsUiState.nextPageState == BaseNextPageState.ERROR
    ) fetchMangaListByCategoryNextPageInternal(currentCategoryDetailsUiState)
  }

  companion object {
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
  }
}
