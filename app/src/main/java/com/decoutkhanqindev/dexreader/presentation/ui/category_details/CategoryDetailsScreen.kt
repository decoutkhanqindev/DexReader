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
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.CategoryDetailsBottomBar
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.CategoryDetailsContent
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
  var isSortSheetVisible by rememberSaveable { mutableStateOf(false) }
  var isFilterSheetVisible by rememberSaveable { mutableStateOf(false) }

  BaseDetailsScreen(
    title = categoryTitle,
    onNavigateBack = onNavigateBack,
    isSearchEnabled = true,
    onSearchClick = onSearchClick,
    bottomBar = {
      CategoryDetailsBottomBar(
        onSortClick = { isSortSheetVisible = true },
        onFilterClick = { isFilterSheetVisible = true },
        modifier = Modifier.fillMaxWidth().height(80.dp)
      )
    },
    content = {
      CategoryDetailsContent(
        categoryDetailsUiState = categoryDetailsUiState,
        categoryCriteriaUiState = categoryCriteriaUiState,
        isSortSheetVisible = isSortSheetVisible,
        onSortSheetDismiss = { isSortSheetVisible = false },
        onSortApplyClick = { criteriaId, orderId ->
          viewModel.updateSortingCriteria(
            criteriaId = criteriaId,
            orderId = orderId
          )
          isSortSheetVisible = false
        },
        isFilterSheetVisible = isFilterSheetVisible,
        onFilterSheetDismiss = { isFilterSheetVisible = false },
        onFilterApplyClick = { statusValueIds, contentRatingValueIds ->
          viewModel.updateFilteringCriteria(
            statusValueIds = statusValueIds,
            contentRatingValueIds = contentRatingValueIds
          )
          isFilterSheetVisible = false
        },
        onSelectedManga = onSelectedManga,
        onFetchMangaListNextPage = { viewModel.fetchMangaListByCategoryNextPage() },
        onRetryFetchMangaListNextPage = { viewModel.retryFetchMangaListByCategoryNextPage() },
        onRetry = { viewModel.retry() },
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}