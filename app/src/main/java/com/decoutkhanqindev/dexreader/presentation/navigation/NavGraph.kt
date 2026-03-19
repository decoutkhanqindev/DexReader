package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
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
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue.Companion.toNavRoute
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
    startDestination = NavRoute.Home,
    modifier = modifier
  ) {
    // Home Screen - slides from LEFT
    slideFromLeftTransitions().let { transitions ->
      composable<NavRoute.Home>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        HomeScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.Home>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onSelectedManga = { mangaId ->
            navController.navigate(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Categories Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Categories>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        CategoriesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.Categories>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onCategoryClick = { categoryId, categoryTitle ->
            navController.navigate(NavRoute.CategoryDetails(categoryId, categoryTitle))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Category Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.CategoryDetails>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        CategoryDetailsScreen(
          onNavigateBack = navController::navigateUp,
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onMangaClick = { mangaId ->
            navController.navigate(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize(),
        )
      }
    }

    // Favorites Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Favorites>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        FavoritesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.Favorites>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onSelectedManga = { mangaId ->
            navController.navigate(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // History Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.History>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        HistoryScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.History>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onContinueReadingClick = { chapterId, lastReadPage, mangaId ->
            navController.navigate(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
          onMangaDetailsClick = { mangaId ->
            navController.navigate(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Profile Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Profile>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        ProfileScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onLogoutSuccess = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Home)
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Settings Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Settings>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        SettingsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onSignInClick = {
            navController.navigateClearStack<NavRoute.Settings>(NavRoute.Login)
          },
          onMenuItemClick = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Search Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Search>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        SearchScreen(
          onNavigateBack = navController::navigateUp,
          onSelectedManga = { mangaId ->
            navController.navigate(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Manga Details Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.MangaDetails>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        MangaDetailsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = navController::navigateUp,
          onSearchClick = {
            navController.navigate(NavRoute.Search)
          },
          onSignInClick = {
            navController.navigateClearStack<NavRoute.MangaDetails>(NavRoute.Login)
          },
          onReadingClick = { chapterId, lastReadPage, mangaId ->
            navController.navigate(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
          onSelectedCategory = { categoryId, categoryTitle ->
            navController.navigate(NavRoute.CategoryDetails(categoryId, categoryTitle))
          },
          onSelectedChapter = { chapterId, lastReadPage, mangaId ->
            navController.navigate(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Reader Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavRoute.Reader>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ReaderScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = navController::navigateUp,
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Login Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Login>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        LoginScreen(
          onLoginSuccess = {
            navController.navigateClearStack<NavRoute.Login>(NavRoute.Home)
          },
          onRegisterClick = {
            navController.navigate(NavRoute.Register)
          },
          onForgotPasswordClick = {
            navController.navigate(NavRoute.ForgotPassword)
          },
          modifier = Modifier.fillMaxSize(),
        )
      }
    }

    // Register Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavRoute.Register>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        RegisterScreen(
          onNavigateBack = navController::navigateUp,
          onRegisterSuccess = {
            navController.navigateClearStack<NavRoute.Register>(NavRoute.Login)
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Forgot Password Screen - ENTER ONLY transition
    slideEnterOnlyTransitions().let { transitions ->
      composable<NavRoute.ForgotPassword>(
        enterTransition = transitions.enter,
        popExitTransition = transitions.popExit
      ) {
        ForgotPasswordScreen(
          onNavigateBack = navController::navigateUp,
          onSubmitSuccess = {
            navController.navigateClearStack<NavRoute.ForgotPassword>(NavRoute.Login)
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}