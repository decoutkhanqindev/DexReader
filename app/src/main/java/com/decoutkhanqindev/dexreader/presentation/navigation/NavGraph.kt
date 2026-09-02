package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginScreen
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterScreen
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.UserViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.language.LanguageViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section.MangaSectionViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.onboarding.OnboardingViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.FavoritesScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.HistoryScreen
import com.decoutkhanqindev.dexreader.presentation.screens.language.LanguageSelectionScreen
import com.decoutkhanqindev.dexreader.presentation.screens.language.LanguageSettingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.main.MainScreen
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.onboarding.OnboardingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.privacy_policy.PrivacyPolicyScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ReaderScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SearchScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.StatisticsScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.util.LanguageManager
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph() {
  val navController = rememberNavController()

  val mangaSectionViewModel: MangaSectionViewModel = hiltViewModel()

  val userViewModel: UserViewModel = hiltViewModel()
  val isUserLoggedIn by userViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
  val currentUser by userViewModel.userProfile.collectAsStateWithLifecycle()

  val settingsViewModel: SettingsViewModel = hiltViewModel()
  val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

  val onboardingViewModel: OnboardingViewModel = hiltViewModel()
  val onboardingUiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()

  val languageViewModel: LanguageViewModel = hiltViewModel()
  val languageUiState by languageViewModel.uiState.collectAsStateWithLifecycle()

  LanguageManager.ProvideAppLanguage(language = languageUiState.appliedLanguage) {
    DexReaderTheme(themeOption = settingsUiState.appliedThemeOption) {
      NavHost(
        navController = navController,
        startDestination = NavRoute.Splash,
        modifier = Modifier
          .fillMaxSize()
          .semantics { testTagsAsResourceId = true }
          .background(color = MaterialTheme.colorScheme.background)
      ) {
        composable<NavRoute.Splash> {
          SplashScreen(
            isOnboardingCompleted = onboardingUiState.isCompleted,
            modifier = Modifier.fillMaxSize(),
            onNavigateToLanguageSelectionScreen = {
              navController.navigateClearStack<NavRoute.Splash>(NavRoute.LanguageSelection)
            },
            onNavigateToMainScreen = {
              navController.navigateClearStack<NavRoute.Splash>(NavRoute.Main)
            },
          )
        }

        composable<NavRoute.LanguageSelection> {
          LanguageSelectionScreen(
            languageViewModel = languageViewModel,
            modifier = Modifier.fillMaxSize(),
            onNavigateToOnboardingScreen = {
              navController.navigateClearStack<NavRoute.LanguageSelection>(NavRoute.Onboarding)
            },
          )
        }

        composable<NavRoute.Settings> {
          SettingsScreen(
            settingsViewModel = settingsViewModel,
            modifier = Modifier.fillMaxSize(),
            onNavigateBack = { navController.navigateBack() },
            onNavigateToLanguageScreen = { navController.navigateTo(NavRoute.LanguageSetting) },
            onNavigateToPrivacyScreen = { navController.navigateTo(NavRoute.PrivacyPolicy) },
          )
        }

        composable<NavRoute.LanguageSetting> {
          LanguageSettingScreen(
            languageViewModel = languageViewModel,
            modifier = Modifier.fillMaxSize(),
            onNavigateBack = { navController.navigateBack() },
          )
        }

        composable<NavRoute.PrivacyPolicy> {
          PrivacyPolicyScreen(
            modifier = Modifier.fillMaxSize(),
            onNavigateBack = { navController.navigateBack() },
          )
        }

        composable<NavRoute.Onboarding> {
          OnboardingScreen(
            onboardingViewModel = onboardingViewModel,
            modifier = Modifier.fillMaxSize(),
            onNavigateToMainScreen = {
              navController.navigateClearStack<NavRoute.Onboarding>(NavRoute.Main)
            },
          )
        }

        composable<NavRoute.Main> {
          MainScreen(
            manageSectionViewModel = mangaSectionViewModel,
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize(),
            onNavigateToLoginScreen = {
              navController.navigateClearStack<NavRoute.Main>(NavRoute.Login)
            },
            onNavigateToSettingsScreen = { navController.navigateTo(NavRoute.Settings) },
            onNavigateToSearchScreen = {
              navController.navigateTo(NavRoute.Search)
            },
            onNavigateToMangaDetailScreen = { mangaId ->
              navController.navigateTo(NavRoute.MangaDetails(mangaId))
            },
            onNavigateToCategoryDetailsScreen = { categoryId, categoryTitle, categoryDescription, initialSortCriteria ->
              navController.navigateTo(
                NavRoute.CategoryDetails(
                  categoryTitle = categoryTitle,
                  categoryId = categoryId,
                  categoryDescription = categoryDescription,
                  initialSortCriteria = initialSortCriteria,
                )
              )
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
            onNavigateToReaderScreen = { chapterId, lastReadPage, mangaId ->
              navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
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
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          FavoritesScreen(
            viewModel = hiltViewModel(mainEntry),
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
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          HistoryScreen(
            viewModel = hiltViewModel(mainEntry),
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
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          StatisticsScreen(
            viewModel = hiltViewModel(mainEntry),
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
              navController.navigateClearStack<NavRoute.Login>(NavRoute.Main)
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
}
