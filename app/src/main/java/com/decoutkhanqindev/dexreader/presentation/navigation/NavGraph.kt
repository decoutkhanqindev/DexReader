package com.decoutkhanqindev.dexreader.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.decoutkhanqindev.dexreader.presentation.ui.home.HomeDestination
import com.decoutkhanqindev.dexreader.presentation.ui.home.HomeScreen
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaDetailsDestination
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.ui.search.SearchDestination
import com.decoutkhanqindev.dexreader.presentation.ui.search.SearchScreen

@Composable
fun NavGraph(
  navController: NavHostController,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController = navController,
    startDestination = HomeDestination.route,
    modifier = modifier
  ) {
    composable(route = HomeDestination.route) {
      HomeScreen(
        onMenuClick = {},
        onSearchClick = { navController.navigate(route = SearchDestination.route) },
        onSelectedManga = { mangaId ->
          navController.navigate(route = "${MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }
    composable(route = SearchDestination.route) {
      SearchScreen(
        onNavigateBack = { navController.navigateUp() },
        onSelectedManga = { mangaId ->
          navController.navigate(route = "${MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }
    composable(
      route = MangaDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(MangaDetailsDestination.mangaIdArg) {
          type = NavType.StringType
        }
      )
    ) {
      MangaDetailsScreen(
        onNavigateBack = { navController.navigateUp() },
        onReadingClick = {},
        onSelectedGenre = {},
        onSelectedChapter = {},
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}