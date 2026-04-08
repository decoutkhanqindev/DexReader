package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.mapper.MenuItemMapper.toNavRoute
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterScreen
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailScreen
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
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
) {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = NavDestination.Home,
    modifier = modifier
  ) {
    // Home Screen - slides from LEFT
    slideFromLeftTransitions().let { transitions ->
      composable<NavDestination.Home>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        HomeScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Home>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigate(NavDestination.MangaDetails(mangaId))
          },
        )
      }
    }

    // Categories Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Categories>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        CategoriesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Categories>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateCategoryDetailScreen = { categoryId, categoryTitle ->
            navController.navigate(NavDestination.CategoryDetails(categoryId, categoryTitle))
          },
        )
      }
    }

    // Category Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.CategoryDetails>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        CategoryDetailScreen(
          onNavigateBack = navController::navigateUp,
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigate(NavDestination.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize(),
        )
      }
    }

    // Favorites Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Favorites>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        FavoritesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Favorites>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigate(NavDestination.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // History Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.History>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        HistoryScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.History>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigate(NavDestination.Reader(chapterId, lastReadPage, mangaId))
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigate(NavDestination.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Profile Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Profile>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        ProfileScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Profile>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToHomeScreen = {
            navController.navigateClearStack<NavDestination.Profile>(NavDestination.Home)
          },
        )
      }
    }

    // Settings Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Settings>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        SettingsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Settings>(NavDestination.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
        )
      }
    }

    // Search Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Search>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        SearchScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateToManDetailScreen = { mangaId ->
            navController.navigate(NavDestination.MangaDetails(mangaId))
          },
          onNavigateBack = navController::navigateUp,
        )
      }
    }

    // Manga Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.MangaDetails>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        MangaDetailsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = navController::navigateUp,
          onNavigateToSearchScreen = {
            navController.navigate(NavDestination.Search)
          },
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.MangaDetails>(NavDestination.Login)
          },
          onNavigateCategoryDetailsScreen = { categoryId, categoryTitle ->
            navController.navigate(NavDestination.CategoryDetails(categoryId, categoryTitle))
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigate(NavDestination.Reader(chapterId, lastReadPage, mangaId))
          },
        )
      }
    }

    // Reader Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavDestination.Reader>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ReaderScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
        ) { navController.navigateUp() }
      }
    }

    // Login Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavDestination.Login>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        LoginScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateToHomeScreen = {
            navController.navigateClearStack<NavDestination.Login>(NavDestination.Home)
          },
          onNavigateToRegisterScreen = {
            navController.navigate(NavDestination.Register)
          },
          onNavigateToForgotPasswordScreen = {
            navController.navigate(NavDestination.ForgotPassword)
          },
        )
      }
    }

    // Register Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavDestination.Register>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        RegisterScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = navController::navigateUp,
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.Register>(NavDestination.Login)
          },
        )
      }
    }

    // Forgot Password Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavDestination.ForgotPassword>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ForgotPasswordScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = navController::navigateUp,
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavDestination.ForgotPassword>(NavDestination.Login)
          },
        )
      }
    }
  }
}