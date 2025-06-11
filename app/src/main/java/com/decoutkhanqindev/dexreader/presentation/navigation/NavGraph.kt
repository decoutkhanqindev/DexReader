package com.decoutkhanqindev.dexreader.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.ForgotPasswordScreen
import com.decoutkhanqindev.dexreader.presentation.ui.auth.login.LoginScreen
import com.decoutkhanqindev.dexreader.presentation.ui.auth.register.RegisterScreen
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
import com.decoutkhanqindev.dexreader.utils.navigateClearStack
import com.decoutkhanqindev.dexreader.utils.navigatePreserveState

@Composable
fun NavGraph(
  isUserLoggedIn: Boolean,
  currentUser: User?,
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
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.HomeDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.HomeDestination.route) {
            navHostController.navigatePreserveState(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onSelectedManga = { mangaId ->
          navHostController.navigate(
            route = "${NavDestination.MangaDetailsDestination.route}/$mangaId"
          )
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.CategoriesDestination.route) {
      CategoriesScreen(
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.CategoriesDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.CategoriesDestination.route) {
            navHostController.navigatePreserveState(itemId)
          }
        },
        onSearchClick = {
          navHostController.navigate(route = NavDestination.SearchDestination.route)
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(
            route = "${NavDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle"
          )
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
          navHostController.navigate(
            route = "${NavDestination.MangaDetailsDestination.route}/$mangaId"
          )
        },
        modifier = Modifier.fillMaxSize(),
      )
    }

    composable(route = NavDestination.FavoriteDestination.route) {
      FavoriteScreen(
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.FavoriteDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.FavoriteDestination.route) {
            navHostController.navigatePreserveState(itemId)
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
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.HistoryDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.HistoryDestination.route) {
            navHostController.navigatePreserveState(itemId)
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
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.ProfileDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.ProfileDestination.route) {
            navHostController.navigatePreserveState(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.SettingsDestination.route) {
      SettingsScreen(
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onSignInClick = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.SettingsDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        onMenuItemClick = { itemId ->
          if (itemId != NavDestination.SettingsDestination.route) {
            navHostController.navigatePreserveState(itemId)
          }
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.SearchDestination.route) {
      SearchScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSelectedManga = { mangaId ->
          navHostController.navigate(
            route = "${NavDestination.MangaDetailsDestination.route}/$mangaId"
          )
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
          navHostController.navigate(
            route = "${NavDestination.ReaderDestination.route}/$firstChapterId"
          )
        },
        onSelectedCategory = { categoryId, categoryTitle ->
          navHostController.navigate(
            route = "${NavDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle"
          )
        },
        onSelectedChapter = { chapterId ->
          navHostController.navigate(
            route = "${NavDestination.ReaderDestination.route}/$chapterId"
          )
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

    composable(route = NavDestination.LoginDestination.route) {
      LoginScreen(
        onLoginSuccess = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.LoginDestination.route,
            destination = NavDestination.HomeDestination.route
          )
        },
        onRegisterClick = {
          navHostController.navigate(route = NavDestination.RegisterDestination.route)
        },
        onForgotPasswordClick = {
          navHostController.navigate(route = NavDestination.ForgotPasswordDestination.route)
        },
        modifier = Modifier.fillMaxSize(),
      )
    }

    composable(route = NavDestination.RegisterDestination.route) {
      RegisterScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onRegisterSuccess = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.RegisterDestination.route,
            destination = NavDestination.LoginDestination.route
          )
        },
        modifier = Modifier.fillMaxSize()
      )
    }

    composable(route = NavDestination.ForgotPasswordDestination.route) {
      ForgotPasswordScreen(
        onNavigateBack = { navHostController.navigateUp() },
        onSubmitSuccess = {
          navHostController.navigateClearStack(
            currentRoute = NavDestination.ForgotPasswordDestination.route,
            destination = NavDestination.LoginDestination.route,
          )
        },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}