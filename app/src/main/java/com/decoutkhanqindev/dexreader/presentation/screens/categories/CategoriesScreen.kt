package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun CategoriesScreen(
  navController: NavHostController,
  categoriesViewModel: CategoriesViewModel,
  modifier: Modifier = Modifier,
) {
  val categoryListUiState by categoriesViewModel.categoryListUiState.collectAsStateWithLifecycle()
  val categoryCoverStates by categoriesViewModel.categoryCoverStates.collectAsStateWithLifecycle()

  BaseScreen(
    selectedTab = BottomTabItemValue.CATEGORIES,
    modifier = modifier,
    onNavigateToSearchScreen = { navController.navigateTo(NavRoute.Search) }
  ) {
    CategoriesContent(
      categoryListUiState = categoryListUiState,
      categoryCoverUiState = categoryCoverStates,
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = { categoryId, categoryTitle, categoryDescription ->
        navController.navigateTo(
          NavRoute.CategoryDetails(
            categoryTitle = categoryTitle,
            categoryId = categoryId,
            categoryDescription = categoryDescription,
            initialSortCriteria = MangaSortCriteriaValue.TRENDING,
          )
        )
      },
      onLoadCover = { categoriesViewModel.loadCategoryCover(it) },
      onRefresh = { categoriesViewModel.refresh() },
      onRetry = { categoriesViewModel.retry() },
    )
  }
}