package com.decoutkhanqindev.dexreader.presentation.ui.category_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

  BaseDetailsScreen(
    title = categoryTitle,
    onNavigateBack = onNavigateBack,
    isSearchEnabled = true,
    onSearchClick = onSearchClick,
    content = {
      CategoryDetailsContent(
        categoryDetailsUiState = categoryDetailsUiState,
        categoryCriteriaUiState = categoryCriteriaUiState,
        onSortApplyClick = { criteriaId, orderId ->
          viewModel.updateSortingCriteria(
            criteriaId = criteriaId,
            orderId = orderId
          )
        },
        onFilterApplyClick = { statusValueIds, contentRatingValueIds ->
          viewModel.updateFilteringCriteria(
            statusValueIds = statusValueIds,
            contentRatingValueIds = contentRatingValueIds
          )
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