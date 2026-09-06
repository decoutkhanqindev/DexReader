package com.decoutkhanqindev.dexreader.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section.MangaSectionViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.home.components.HomeContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun HomeScreen(
  navController: NavHostController,
  mangaSectionViewModel: MangaSectionViewModel,
  modifier: Modifier = Modifier,
) {
  val mangaSectionUiState by mangaSectionViewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    selectedTab = BottomTabItemValue.HOME,
    modifier = modifier,
    onNavigateToSearchScreen = { navController.navigateTo(NavRoute.Search) }
  ) {
    HomeContent(
      mangaSectionUiState = mangaSectionUiState,
      modifier = Modifier.fillMaxSize(),
      onItemClick = { mangaId -> navController.navigateTo(NavRoute.MangaDetails(mangaId)) },
      onMoreClick = { categoryTitle, sortCriteria ->
        navController.navigateTo(
          NavRoute.CategoryDetails(
            categoryTitle = categoryTitle,
            initialSortCriteria = sortCriteria,
          )
        )
      },
      onRetry = { mangaSectionViewModel.retry() },
      onRefresh = { mangaSectionViewModel.fetchMangaLists() }
    )
  }
}
