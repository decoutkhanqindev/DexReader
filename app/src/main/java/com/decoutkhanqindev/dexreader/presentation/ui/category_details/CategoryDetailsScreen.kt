package com.decoutkhanqindev.dexreader.presentation.ui.category_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.CategoryDetailsContent
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions.SortAndFilterBottomBar
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseDetailsScreen

@Composable
fun CategoryDetailsScreen(
  onNavigateBack: () -> Unit,
  onSearchClick: () -> Unit,
  onSelectedManga: (String) -> Unit,
  viewModel: CategoryDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val categoryDetailsUiState by viewModel.categoryDetailsUiState.collectAsStateWithLifecycle()
  val categoryCriteriaUiState by viewModel.categoryCriteriaUiState.collectAsStateWithLifecycle()
  val categoryTitle = viewModel.categoryTitleFromArg
  var isShowSortBottomSheet by rememberSaveable { mutableStateOf(false) }
  var isShowFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

  BaseDetailsScreen(
    title = categoryTitle,
    onNavigateBack = onNavigateBack,
    onSearchClick = onSearchClick,
    bottomBarContent = {
      SortAndFilterBottomBar(
        onSortClick = { isShowSortBottomSheet = true },
        onFilterClick = { isShowFilterBottomSheet = true },
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
      )
    },
    content = {
      CategoryDetailsContent(
        categoryDetailsUiState = categoryDetailsUiState,
        categoryCriteriaUiState = categoryCriteriaUiState,
        isSortBottomSheetVisible = isShowSortBottomSheet,
        onSortSheetDismiss = { isShowSortBottomSheet = false },
        onSortApplyClick = { criteriaId, orderId ->
          viewModel.updateSortingCriteria(
            criteriaId = criteriaId,
            orderId = orderId
          )
          isShowSortBottomSheet = false
        },
        isFilterBottomSheetVisible = isShowFilterBottomSheet,
        onFilterSheetDismiss = { isShowFilterBottomSheet = false },
        onFilterApplyClick = { statusValueIds, contentRatingValueIds ->
          viewModel.updateFilteringCriteria(
            statusValueIds = statusValueIds,
            contentRatingValueIds = contentRatingValueIds
          )
          isShowFilterBottomSheet = false
        },
        onSelectedManga = onSelectedManga,
        onFetchMangaListNextPage = viewModel::fetchMangaListByCategoryNextPage,
        onRetryFetchMangaListNextPage = viewModel::retryFetchMangaListByCategoryNextPage,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}