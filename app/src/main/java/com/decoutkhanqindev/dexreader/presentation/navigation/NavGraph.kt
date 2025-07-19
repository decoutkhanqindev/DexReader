package com.decoutkhanqindev.dexreader.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
import com.decoutkhanqindev.dexreader.utils.navigateClearStack
import com.decoutkhanqindev.dexreader.utils.navigatePreserveState

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
    val animationDuration = 500
    val offsetX = 500
    val slideAnimationSpec: FiniteAnimationSpec<IntOffset> = tween(
      durationMillis = animationDuration,
      easing = FastOutSlowInEasing
    )
    val fadeAnimationSpec: FiniteAnimationSpec<Float> = tween(animationDuration)

    // Home Screen
    composable(
      route = NavDestination.HomeDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Categories Screen
    composable(
      route = NavDestination.CategoriesDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Category Details Screen
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
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Favorites Screen
    composable(
      route = NavDestination.FavoritesDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // History Screen
    composable(
      route = NavDestination.HistoryDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Profile Screen
    composable(
      route = NavDestination.ProfileDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Settings Screen
    composable(
      route = NavDestination.SettingsDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Search Screen
    composable(
      route = NavDestination.SearchDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Manga Details Screen
    composable(
      route = NavDestination.MangaDetailsDestination.routeWithArgs,
      arguments = listOf(
        navArgument(NavDestination.MangaDetailsDestination.MANGA_ID_ARG) {
          type = NavType.StringType
        }
      ),
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Reader Screen
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
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
    ) {
      ReaderScreen(
        isUserLoggedIn = isUserLoggedIn,
        currentUser = currentUser,
        onNavigateBack = navHostController::navigateUp,
        modifier = Modifier.fillMaxSize()
      )
    }

    // Login Screen
    composable(
      route = NavDestination.LoginDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      exitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      },
      popEnterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Register Screen
    composable(
      route = NavDestination.RegisterDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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

    // Forgot Password Screen
    composable(
      route = NavDestination.ForgotPasswordDestination.route,
      enterTransition = {
        slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { offsetX }) +
            fadeIn(animationSpec = fadeAnimationSpec)
      },
      popExitTransition = {
        slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { offsetX }) +
            fadeOut(animationSpec = fadeAnimationSpec)
      }
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