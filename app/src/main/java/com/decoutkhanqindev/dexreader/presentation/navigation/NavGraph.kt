package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.mapper.MenuMapper.toNavRoute
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterScreen
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.UserViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.FavoritesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.HistoryScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeScreen
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ReaderScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SearchScreen
import com.decoutkhanqindev.dexreader.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.StatisticsScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigatePreserveState
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun NavGraph() {
  val navController = rememberNavController()

  val userViewModel: UserViewModel = hiltViewModel()
  val isUserLoggedIn by userViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
  val currentUser by userViewModel.userProfile.collectAsStateWithLifecycle()

  val settingsViewModel: SettingsViewModel = hiltViewModel()
  val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

  DexReaderTheme(themeOption = settingsUiState.appliedThemeOption) {
    NavHost(
      navController = navController,
      startDestination = NavRoute.Splash,
      modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
    ) {
      composable<NavRoute.Splash> {
        SplashScreen(
          modifier = Modifier.fillMaxSize(),
          onNavigateToHome = {
            navController.navigateClearStack<NavRoute.Splash>(NavRoute.Home)
          },
        )
      }

      composable<NavRoute.Home> {
        HomeScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Home>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState<NavRoute.Home>(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
          },
          onNavigateToSectionDetailsScreen = { title, sortCriteria ->
            navController.navigateTo(
              NavRoute.CategoryDetails(
                categoryTitle = title,
                categoryId = null,
                initialSortCriteria = sortCriteria,
              )
            )
          },
        )
      }

      composable<NavRoute.Categories> {
        CategoriesScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Categories>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState<NavRoute.Home>(item.toNavRoute())
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          onNavigateToCategoryScreen = { categoryId, categoryTitle, categoryDescription ->
            navController.navigateTo(
              NavRoute.CategoryDetails(
                categoryTitle = categoryTitle,
                categoryId = categoryId,
                categoryDescription = categoryDescription,
                initialSortCriteria = MangaSortCriteriaValue.TRENDING,
              )
            )
          },
        )
      }

      composable<NavRoute.CategoryDetails> {
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

      composable<NavRoute.Favorites> {
        val profileEntry = remember(it) { navController.getBackStackEntry<NavRoute.Profile>() }
        FavoritesScreen(
          viewModel = hiltViewModel(profileEntry),
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = {
            navController.navigateBack()
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

      composable<NavRoute.History> {
        val profileEntry = remember(it) { navController.getBackStackEntry<NavRoute.Profile>() }
        HistoryScreen(
          viewModel = hiltViewModel(profileEntry),
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = {
            navController.navigateBack()
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

      composable<NavRoute.Statistics> {
        val profileEntry = remember(it) { navController.getBackStackEntry<NavRoute.Profile>() }
        StatisticsScreen(
          viewModel = hiltViewModel(profileEntry),
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          onNavigateBack = {
            navController.navigateBack()
          },
          onNavigateToSearchScreen = {
            navController.navigateTo(NavRoute.Search)
          },
          modifier = Modifier.fillMaxSize()
        )
      }

      composable<NavRoute.Profile> {
        ProfileScreen(
          settingsViewModel = settingsViewModel,
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
          onNavigateToLoginScreen = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Login)
          },
          onNavigateToMenuItemScreen = { item ->
            navController.navigatePreserveState<NavRoute.Home>(item.toNavRoute())
          },
          onNavigateToHomeScreen = {
            navController.navigateClearStack<NavRoute.Profile>(NavRoute.Home)
          },
          onNavigateToFavoritesScreen = {
            navController.navigateTo(NavRoute.Favorites)
          },
          onNavigateToHistoryScreen = {
            navController.navigateTo(NavRoute.History)
          },
          onNavigateToStatisticsScreen = {
            navController.navigateTo(NavRoute.Statistics)
          },
          onNavigateToMangaDetailScreen = { mangaId ->
            navController.navigateTo(NavRoute.MangaDetails(mangaId))
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
        )
      }

      composable<NavRoute.Search> {
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

      composable<NavRoute.MangaDetails> {
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
          onNavigateCategoryDetailsScreen = { categoryId, categoryTitle, categoryDescription ->
            navController.navigateTo(
              NavRoute.CategoryDetails(
                categoryTitle = categoryTitle,
                categoryId = categoryId,
                categoryDescription = categoryDescription,
              )
            )
          },
          onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
            navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
          },
        )
      }

      composable<NavRoute.Reader> {
        ReaderScreen(
          isUserLoggedIn = isUserLoggedIn,
          currentUser = currentUser,
          modifier = Modifier.fillMaxSize(),
        ) { navController.navigateBack() }
      }

      composable<NavRoute.Login> {
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

      composable<NavRoute.Register> {
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

      composable<NavRoute.ForgotPassword> {
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