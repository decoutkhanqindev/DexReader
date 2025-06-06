package com.decoutkhanqindev.dexreader.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.decoutkhanqindev.dexreader.presentation.ui.categories.CategoriesScreen
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.CategoryDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.ui.favorite.FavoriteScreen
import com.decoutkhanqindev.dexreader.presentation.ui.history.HistoryScreen
import com.decoutkhanqindev.dexreader.presentation.ui.home.HomeScreen
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.ui.profile.ProfileScreen
import com.decoutkhanqindev.dexreader.presentation.ui.reader.ReaderScreen
import com.decoutkhanqindev.dexreader.presentation.ui.search.SearchScreen
import com.decoutkhanqindev.dexreader.presentation.ui.settings.SettingsScreen

@Composable
fun NavGraph(
  navHostController: NavHostController,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController = navHostController,
    startDestination = NavigationDestination.HomeDestination.route,
    modifier = modifier
  ) {
    composable(route = NavigationDestination.HomeDestination.route) {
      HomeScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.CategoriesDestination.route) {
      CategoriesScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.CategoriesDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(route = "${NavigationDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.CategoryDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.CategoryDetailsDestination.CATEGORY_ID_ARG) {
          type = NavType.StringType
        },
        navArgument(NavigationDestination.CategoryDetailsDestination.CATEGORY_TITLE_ARG) {
          type = NavType.StringType
        },
      )
    ) {
      CategoryDetailsScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize(),
      )
    }

    composable(route = NavigationDestination.FavoriteDestination.route) {
      FavoriteScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.FavoriteDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.HistoryDestination.route) {
      HistoryScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.ProfileDestination.route) {
      ProfileScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.ProfileDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SettingsDestination.route) {
      SettingsScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.SettingsDestination.route) {
            navHostController.navigate(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SearchDestination.route) {
      SearchScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.MangaDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.MangaDetailsDestination.MANGA_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      MangaDetailsScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchDestination.route)
        },
        onReadingClick = { firstChapterId ->
          navHostController.navigate(route = "${NavigationDestination.ReaderDestination.route}/$firstChapterId")
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(route = "${NavigationDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle")
        },
        onSelectedChapter = { chapterId ->
          navHostController.navigate(route = "${NavigationDestination.ReaderDestination.route}/$chapterId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.ReaderDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.ReaderDestination.CHAPTER_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      ReaderScreen(
        onNavigateBack = { navHostController.navigateUp() },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}