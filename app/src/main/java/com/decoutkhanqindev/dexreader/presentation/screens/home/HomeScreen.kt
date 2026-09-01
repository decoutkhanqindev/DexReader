package com.decoutkhanqindev.dexreader.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section.MangaSectionViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.home.components.HomeContent

@Composable
fun HomeScreen(
  mangaSectionViewModel: MangaSectionViewModel,
  modifier: Modifier = Modifier,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  onNavigateToSectionDetailsScreen: (categoryTitle: String, sortCriteria: MangaSortCriteriaValue) -> Unit,
) {
  val mangaSectionUiState by mangaSectionViewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    selectedTab = BottomTabItemValue.HOME,
    modifier = modifier,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    HomeContent(
      mangaSectionUiState = mangaSectionUiState,
      modifier = Modifier.fillMaxSize(),
      onItemClick = onNavigateToMangaDetailScreen,
      onMoreClick = onNavigateToSectionDetailsScreen,
      onRetry = { mangaSectionViewModel.retry() },
      onRefresh = { mangaSectionViewModel.fetchMangaLists() }
    )
  }
}
