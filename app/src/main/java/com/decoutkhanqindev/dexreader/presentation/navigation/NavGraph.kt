package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.mapper.MenuMapper.toNavRoute
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
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.StatisticsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ReaderScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SearchScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigatePreserveState
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo
import com.decoutkhanqindev.dexreader.util.NavTransitions.slideEnterOnlyTransitions
import com.decoutkhanqindev.dexreader.util.NavTransitions.slideExitOnlyTransitions
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
    startDestination = NavRoute.Splash,
    modifier = modifier
  ) {
    // Splash Screen - EXIT ONLY (start destination, slides out left when Home enters)
    composable<NavRoute.Splash>(
      exitTransition = slideExitOnlyTransitions().exit,
    ) {
      SplashScreen(
        modifier = Modifier.fillMaxSize(),
        onNavigateToHome = {
          navController.navigateClearStack<NavRoute.Splash>(NavRoute.Home)
        },
      )
    }

    // Home Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Home>(
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
            navController.navigateClearStack<NavRoute.Home>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Categories>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateCategoryDetailScreen = { categoryId, categoryTitle ->
            navController.navigateTo(NavRoute.CategoryDetails(categoryId, categoryTitle)
            )
          },
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
        CategoryDetailScreen(
          onNavigateBack = {
            navController.navigateBack()
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
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
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Favorites>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
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
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.History>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Statistics Screen - slides from RIGHT
    slideFromRightTransitions().let { transitions ->
      composable<NavRoute.Statistics>(
        enterTransition = transitions.enter,
        exitTransition = transitions.exit,
        popEnterTransition = transitions.popEnter,
        popExitTransition = transitions.popExit
      ) {
        StatisticsScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Statistics>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
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
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
          onNavigateToHomeScreen = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Home)
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Settings>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState(item.toNavRoute())
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateToManDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
          },
          onNavigateBack = {
            navController.navigateBack()
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = {
            navController.navigateBack()
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.MangaDetails>(NavRoute.Login)
          },
          onNavigateCategoryDetailsScreen = { categoryId, categoryTitle ->
            navController.navigateTo(
              NavRoute.CategoryDetails(
                categoryId,
                categoryTitle
              )
            )
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
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
          modifier = Modifier.fillMaxSize(),
        ) { navController.navigateBack() }
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
          modifier = Modifier.fillMaxSize(),
          onNavigateToHomeScreen = {
            navController.navigateClearStack<NavRoute.Login>(NavRoute.Home)
          },
          onNavigateToRegisterScreen = {
            navController.navigateTo(NavRoute.Register)
          },
          onNavigateToForgotPasswordScreen = {
            navController.navigateTo(NavRoute.ForgotPassword)
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = {
            navController.navigateBack()
          },
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Register>(NavRoute.Login)
          },
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
          modifier = Modifier.fillMaxSize(),
          onNavigateBack = {
            navController.navigateBack()
          },
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.ForgotPassword>(NavRoute.Login)
          },
        )
      }
    }
  }
}