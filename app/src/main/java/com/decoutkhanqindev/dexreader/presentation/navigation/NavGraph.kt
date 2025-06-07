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
import com.decoutkhanqindev.dexreader.utils.navigateTo

@Composable
fun NavGraph(
  navHostController: NavHostController,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController = navHostController,
    startDestination = NavDestination.HomeDestination.route,
    modifier = modifier
  ) {
    composable(route = NavDestination.HomeDestination.route) {
      HomeScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.HistoryDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.CategoriesDestination.route) {
      CategoriesScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.CategoriesDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(route = "${NavDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavDestination.CategoryDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavDestination.CategoryDetailsDestination.CATEGORY_ID_ARG) {
          type = NavType.StringType
        },
        navArgument(NavDestination.CategoryDetailsDestination.CATEGORY_TITLE_ARG) {
          type = NavType.StringType
        },
      )
    ) {
      CategoryDetailsScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize(),
      )
    }

    composable(route = NavDestination.FavoriteDestination.route) {
      FavoriteScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.FavoriteDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.HistoryDestination.route) {
      HistoryScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.HistoryDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.ProfileDestination.route) {
      ProfileScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.ProfileDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.SettingsDestination.route) {
      SettingsScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.SettingsDestination.route) {
            navHostController.navigateTo(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.SearchDestination.route) {
      SearchScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavDestination.MangaDetailsDestination.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavDestination.MangaDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavDestination.MangaDetailsDestination.MANGA_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      MangaDetailsScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onReadingClick = { firstChapterId ->
          navHostController.navigate(route = "${NavDestination.ReaderDestination.route}/$firstChapterId")
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(route = "${NavDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle")
        },
        onSelectedChapter = { chapterId ->
          navHostController.navigate(route = "${NavDestination.ReaderDestination.route}/$chapterId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavDestination.ReaderDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavDestination.ReaderDestination.CHAPTER_ID_ARG) {
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