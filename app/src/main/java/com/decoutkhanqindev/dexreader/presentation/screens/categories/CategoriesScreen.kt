package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen

@Composable
fun CategoriesScreen(
  categoriesViewModel: CategoriesViewModel,
  modifier: Modifier = Modifier,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToCategoryScreen: (categoryId: String, title: String, description: String) -> Unit,
) {
  val categoryListUiState by categoriesViewModel.categoryListUiState.collectAsStateWithLifecycle()
  val categoryCoverStates by categoriesViewModel.categoryCoverStates.collectAsStateWithLifecycle()

  BaseScreen(
    selectedTab = BottomTabItemValue.CATEGORIES,
    modifier = modifier,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    CategoriesContent(
      categoryListUiState = categoryListUiState,
      categoryCoverUiState = categoryCoverStates,
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = onNavigateToCategoryScreen,
      onLoadCover = { categoriesViewModel.loadCategoryCover(it) },
      onRefresh = { categoriesViewModel.refresh() },
      onRetry = { categoriesViewModel.retry() },
    )
  }
}