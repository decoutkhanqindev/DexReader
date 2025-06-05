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
  navController: NavHostController,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController = navController,
    startDestination = NavigationDestination.HomeScreen.route,
    modifier = modifier
  ) {
    composable(route = NavigationDestination.HomeScreen.route) {
      HomeScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onSelectedManga = { mangaId ->
          navController.navigate(route = "${NavigationDestination.MangaDetailsScreen.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.CategoriesScreen.route) {
      CategoriesScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.CategoriesScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.FavoriteScreen.route) {
      FavoriteScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.FavoriteScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.HistoryScreen.route) {
      HistoryScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.ProfileScreen.route) {
      ProfileScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.ProfileScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SettingsScreen.route) {
      SettingsScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.SettingsScreen.route) {
            navController.navigate(itemId)
          }
        },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SearchScreen.route) {
      SearchScreen(
        onNavigateBack = { navController.navigateUp() },
        onSelectedManga = { mangaId ->
          navController.navigate(route = "${NavigationDestination.MangaDetailsScreen.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.MangaDetailsScreen.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.MangaDetailsScreen.MANGA_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      MangaDetailsScreen(
        onNavigateBack = { navController.navigateUp() },
        onSearchClick = {
          navController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onReadingClick = { firstChapterId ->
          navController.navigate(route = "${NavigationDestination.ReaderScreen.route}/$firstChapterId")
        },
        onSelectedGenre = {},
        onSelectedChapter = { chapterId ->
          navController.navigate(route = "${NavigationDestination.ReaderScreen.route}/$chapterId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.ReaderScreen.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.ReaderScreen.CHAPTER_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      ReaderScreen(
        onNavigateBack = { navController.navigateUp() },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}