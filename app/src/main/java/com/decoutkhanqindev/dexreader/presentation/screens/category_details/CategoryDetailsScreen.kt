package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.CategoryDetailsContent
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.actions.SortAndFilterButtons
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen

@Composable
fun CategoryDetailScreen(
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  viewModel: CategoryDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val detailsUiState by viewModel.categoryDetailsUiState.collectAsStateWithLifecycle()
  val criteriaUiState by viewModel.categoryCriteriaUiState.collectAsStateWithLifecycle()
  val categoryTitle = viewModel.categoryTitleFromArg

  BaseDetailsScreen(
    title = categoryTitle,
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    modifier = modifier,
  ) {
    CategoryDetailsContent(
      detailsUiState = detailsUiState,
      criteriaUiState = criteriaUiState,
      onSortApplyClick = { sortCriteria, sortOrder ->
        viewModel.updateSortingCriteria(sortCriteria, sortOrder)
      },
      onFilterApplyClick = { statusFilter, contentRatingFilter ->
        viewModel.updateFilteringCriteria(statusFilter, contentRatingFilter)
      },
      onMangaClick = onNavigateToMangaDetailScreen,
      onFetchMangaListNextPage = viewModel::fetchMangaListByCategoryNextPage,
      onRetryFetchMangaListNextPage = viewModel::retryFetchMangaListByCategoryNextPage,
      onRetry = viewModel::retry,
      modifier = Modifier.fillMaxSize()
    )
  }
}