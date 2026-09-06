package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.CategoryDetailsContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun CategoryDetailScreen(
  navController: NavHostController,
  viewModel: CategoryDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val detailsUiState by viewModel.categoryDetailsUiState.collectAsStateWithLifecycle()
  val criteriaUiState by viewModel.categoryCriteriaUiState.collectAsStateWithLifecycle()
  val categoryTitle = viewModel.categoryTitleFromArg
  val categoryDescription = viewModel.categoryDescriptionFromArg

  BackHandler { navController.navigateBack() }

  BaseDetailsScreen(
    title = categoryTitle,
    onNavigateBack = { navController.navigateBack() },
    onNavigateToSearchScreen = { navController.navigateTo(NavRoute.Search) },
    modifier = modifier,
  ) {
    CategoryDetailsContent(
      detailsUiState = detailsUiState,
      criteriaUiState = criteriaUiState,
      categoryDescription = categoryDescription,
      onSortApplyClick = { s, o -> viewModel.updateSortingCriteria(s, o) },
      onFilterApplyClick = { s, c -> viewModel.updateFilteringCriteria(s, c) },
      onMangaClick = { mangaId -> navController.navigateTo(NavRoute.MangaDetails(mangaId)) },
      onFetchMangaListNextPage = { viewModel.fetchMangaListByCategoryNextPage() },
      onRetryFetchMangaListNextPage = { viewModel.retryFetchMangaListByCategoryNextPage() },
      onRetry = { viewModel.retry() },
      onRefresh = { viewModel.refresh() },
      modifier = Modifier.fillMaxSize()
    )
  }
}