package com.decoutkhanqindev.dexreader.presentation.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.mapper.BottomTabItemMapper.toNavRoute
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.bottom_bar.AppBottomBar
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.favorites.FavoritesViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.history.HistoryViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section.MangaSectionViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics.StatisticsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileScreen
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigatePreserveState
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MainScreen(
  manageSectionViewModel: MangaSectionViewModel,
  settingsViewModel: SettingsViewModel,
  favoritesViewModel: FavoritesViewModel = hiltViewModel(),
  historyViewModel: HistoryViewModel = hiltViewModel(),
  statisticsViewModel: StatisticsViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  onNavigateToCategoryDetailsScreen: (
    categoryId: String?,
    categoryTitle: String,
    categoryDescription: String,
    initialSortCriteria: MangaSortCriteriaValue,
  ) -> Unit,
  onNavigateToFavoritesScreen: () -> Unit,
  onNavigateToHistoryScreen: () -> Unit,
  onNavigateToStatisticsScreen: () -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
) {
  val navController = rememberNavController()
  val currentBackStackEntry by navController.currentBackStackEntryAsState()
  val selectedTab = remember(currentBackStackEntry) {
    val destination = currentBackStackEntry?.destination
    when {
      destination?.hasRoute(NavRoute.Categories::class) == true -> BottomTabItemValue.CATEGORIES
      destination?.hasRoute(NavRoute.Profile::class) == true -> BottomTabItemValue.PROFILE
      else -> BottomTabItemValue.HOME
    }
  }

  BackHandler {}

  Box(modifier = modifier.fillMaxSize()) {
    NavHost(
      navController = navController,
      startDestination = NavRoute.Home,
      modifier = Modifier.fillMaxSize()
    ) {
      composable<NavRoute.Home> {
        HomeScreen(
          mangaSectionViewModel = manageSectionViewModel,
          modifier = Modifier.fillMaxSize(),
          onNavigateToSearchScreen = onNavigateToSearchScreen,
          onNavigateToMangaDetailScreen = onNavigateToMangaDetailScreen,
          onNavigateToSectionDetailsScreen = { title, sortCriteria ->
            onNavigateToCategoryDetailsScreen(null, title, "", sortCriteria)
          },
        )
      }

      composable<NavRoute.Categories> {
        CategoriesScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateToSearchScreen = onNavigateToSearchScreen,
          onNavigateToCategoryScreen = { categoryId, categoryTitle, categoryDescription ->
            onNavigateToCategoryDetailsScreen(
              categoryId,
              categoryTitle,
              categoryDescription,
              MangaSortCriteriaValue.TRENDING,
            )
          },
        )
      }

      composable<NavRoute.Profile> {
        ProfileScreen(
          settingsViewModel = settingsViewModel,
          favoritesViewModel = favoritesViewModel,
          historyViewModel = historyViewModel,
          statisticsViewModel = statisticsViewModel,
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = onNavigateToLoginScreen,
          onNavigateToFavoritesScreen = onNavigateToFavoritesScreen,
          onNavigateToHistoryScreen = onNavigateToHistoryScreen,
          onNavigateToStatisticsScreen = onNavigateToStatisticsScreen,
          onNavigateToMangaDetailScreen = onNavigateToMangaDetailScreen,
          onNavigateToReaderScreen = onNavigateToReaderScreen,
        )
      }
    }

    AppBottomBar(
      selectedItem = selectedTab,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .blurBackground(alphas = persistentListOf(0f, 0.9f, 1f, 1f))
        .navigationBarsPadding()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      onItemClick = { tab ->
        navController.navigatePreserveState<NavRoute.Home>(tab.toNavRoute())
      },
    )
  }
}
