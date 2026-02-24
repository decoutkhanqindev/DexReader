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
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterScreen
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.FavoritesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.HistoryScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeScreen
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ReaderScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SearchScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigatePreserveState
import com.decoutkhanqindev.dexreader.util.NavTransitions.slideEnterOnlyTransitions
import com.decoutkhanqindev.dexreader.util.NavTransitions.slideFromLeftTransitions
import com.decoutkhanqindev.dexreader.util.NavTransitions.slideFromRightTransitions

@Composable
fun NavGraph(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  navHostController: NavHostController,
  modifier: Modifier = Modifier,
) {
  NavHost(
    navController = navHostController,
    startDestination = NavDestination.HomeDestination.route,
    modifier = modifier
  ) {
    // Home Screen - slides from LEFT
    slideFromLeftTransitions().let { transitions ->
      composable(
        route = NavDestination.HomeDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
    }

    // Categories Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.CategoriesDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
    }

    // Category Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.CategoryDetailsDestination.routeWithArgs,
        arguments = listOf(
          navArgument(NavDestination.CategoryDetailsDestination.CATEGORY_ID_ARG) {
            type = NavType.StringType
          },
          navArgument(NavDestination.CategoryDetailsDestination.CATEGORY_TITLE_ARG) {
            type = NavType.StringType
          },
        ),
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        CategoryDetailsScreen(
          onNavigateBack = navHostController::navigateUp,
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
    }

    // Favorites Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.FavoritesDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        FavoritesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navHostController.navigateClearStack(
              currentRoute = NavDestination.FavoritesDestination.route,
              destination = NavDestination.LoginDestination.route
            )
          },
          onMenuItemClick = { itemId ->
            if (itemId != NavDestination.FavoritesDestination.route) {
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
    }

    // History Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.HistoryDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
          onContinueReadingClick = { chapterId, lastReadPage, mangaId ->
            navHostController.navigate(
              route = "${NavDestination.ReaderDestination.route}/$chapterId/$lastReadPage/$mangaId"
            )
          },
          onMangaDetailsClick = { mangaId ->
            navHostController.navigate(
              route = "${NavDestination.MangaDetailsDestination.route}/$mangaId"
            )
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Profile Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.ProfileDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
          onLogoutSuccess = {
            navHostController.navigateClearStack(
              currentRoute = NavDestination.ProfileDestination.route,
              destination = NavDestination.HomeDestination.route,
            )
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Settings Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.SettingsDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
    }

    // Search Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.SearchDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        SearchScreen(
          onNavigateBack = navHostController::navigateUp,
          onSelectedManga = { mangaId ->
            navHostController.navigate(
              route = "${NavDestination.MangaDetailsDestination.route}/$mangaId"
            )
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Manga Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.MangaDetailsDestination.routeWithArgs,
        arguments = listOf(
          navArgument(NavDestination.MangaDetailsDestination.MANGA_ID_ARG) {
            type = NavType.StringType
          }
        ),
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        MangaDetailsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = navHostController::navigateUp,
          onSearchClick = {
            navHostController.navigate(route = NavDestination.SearchDestination.route)
          },
          onSignInClick = {
            navHostController.navigateClearStack(
              currentRoute = NavDestination.MangaDetailsDestination.route,
              destination = NavDestination.LoginDestination.route
            )
          },
          onReadingClick = { chapterId, lastReadPage, mangaId ->
            navHostController.navigate(
              route = "${NavDestination.ReaderDestination.route}/$chapterId/${lastReadPage}/${mangaId}"
            )
          },
          onSelectedCategory = { categoryId, categoryTitle ->
            navHostController.navigate(
              route = "${NavDestination.CategoryDetailsDestination.route}/$categoryId/$categoryTitle"
            )
          },
          onSelectedChapter = { chapterId, lastReadPage, mangaId ->
            navHostController.navigate(
              route = "${NavDestination.ReaderDestination.route}/$chapterId/${lastReadPage}/${mangaId}"
            )
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Reader Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable(
        route = NavDestination.ReaderDestination.routeWithArgs,
        arguments = listOf(
          navArgument(NavDestination.ReaderDestination.CHAPTER_ID_ARG) {
            type = NavType.StringType
          },
          navArgument(NavDestination.ReaderDestination.LAST_READ_PAGE_ARG) {
            type = NavType.IntType
            defaultValue = 0
            nullable = false
          },
          navArgument(NavDestination.ReaderDestination.MANGA_ID_ARG) {
            type = NavType.StringType
          },
        ),
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ReaderScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = navHostController::navigateUp,
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Login Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable(
        route = NavDestination.LoginDestination.route,
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
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
    }

    // Register Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable(
        route = NavDestination.RegisterDestination.route,
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        RegisterScreen(
          onNavigateBack = navHostController::navigateUp,
          onRegisterSuccess = {
            navHostController.navigateClearStack(
              currentRoute = NavDestination.RegisterDestination.route,
              destination = NavDestination.LoginDestination.route
            )
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Forgot Password Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable(
        route = NavDestination.ForgotPasswordDestination.route,
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ForgotPasswordScreen(
          onNavigateBack = navHostController::navigateUp,
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
}
