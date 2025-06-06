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
    startDestination = NavigationDestination.HomeScreen.route,
    modifier = modifier
  ) {
    composable(route = NavigationDestination.HomeScreen.route) {
      HomeScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsScreen.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.CategoriesScreen.route) {
      CategoriesScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.CategoriesScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onSelectedCategory = { tagId ->
          navHostController.navigate(route = "${NavigationDestination.CategoryDetailsScreen.route}/$tagId")
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(
      route = NavigationDestination.CategoryDetailsScreen.routeWithArgs,
      arguments = listOf(
        navArgument(NavigationDestination.CategoryDetailsScreen.TAG_ID_ARG) {
          type = NavType.StringType
        }
      )
    ) {
      CategoryDetailsScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsScreen.route}/$mangaId")
        },
        modifier = Modifier.fillMaxSize(),
      )
    }

    composable(route = NavigationDestination.FavoriteScreen.route) {
      FavoriteScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.FavoriteScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.HistoryScreen.route) {
      HistoryScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.HistoryScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.ProfileScreen.route) {
      ProfileScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.ProfileScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SettingsScreen.route) {
      SettingsScreen(
        onMenuItemClick = { itemId ->
          if (itemId != NavigationDestination.SettingsScreen.route) {
            navHostController.navigate(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavigationDestination.SearchScreen.route) {
      SearchScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSelectedManga = { mangaId ->
          navHostController.navigate(route = "${NavigationDestination.MangaDetailsScreen.route}/$mangaId")
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
        onNavigateBack = { navHostController.navigateUp() },
        onSearchClick = {
          navHostController.navigate(route = NavigationDestination.SearchScreen.route)
        },
        onReadingClick = { firstChapterId ->
          navHostController.navigate(route = "${NavigationDestination.ReaderScreen.route}/$firstChapterId")
        },
        onSelectedCategory = { categoryId ->
          navHostController.navigate(route = "${NavigationDestination.CategoryDetailsScreen.route}/$categoryId")
        },
        onSelectedChapter = { chapterId ->
          navHostController.navigate(route = "${NavigationDestination.ReaderScreen.route}/$chapterId")
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
        onNavigateBack = { navHostController.navigateUp() },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}