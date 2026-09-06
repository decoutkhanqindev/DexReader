package com.decoutkhanqindev.dexreader.presentation.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
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
            navController = navController,
            isOnboardingCompleted = onboardingUiState.isCompleted,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.LanguageSelection> {
          LanguageSelectionScreen(
            navController = navController,
            languageViewModel = languageViewModel,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Onboarding> {
          OnboardingScreen(
            navController = navController,
            onboardingViewModel = onboardingViewModel,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Main> {
          MainScreen(
            navController = navController,
            manageSectionViewModel = mangaSectionViewModel,
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Search> {
          SearchScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.MangaDetails> {
          MangaDetailsScreen(
            navController = navController,
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.CategoryDetails> {
          CategoryDetailScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Reader> {
          ReaderScreen(
            navController = navController,
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Favorites> {
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          FavoritesScreen(
            navController = navController,
            viewModel = hiltViewModel(mainEntry),
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize()
          )
        }

        composable<NavRoute.History> {
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          HistoryScreen(
            navController = navController,
            viewModel = hiltViewModel(mainEntry),
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize()
          )
        }

        composable<NavRoute.Statistics> {
          val mainEntry = remember(it) { navController.getBackStackEntry<NavRoute.Main>() }
          StatisticsScreen(
            navController = navController,
            viewModel = hiltViewModel(mainEntry),
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier.fillMaxSize()
          )
        }

        composable<NavRoute.Settings> {
          SettingsScreen(
            navController = navController,
            settingsViewModel = settingsViewModel,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.LanguageSetting> {
          LanguageSettingScreen(
            navController = navController,
            languageViewModel = languageViewModel,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.PrivacyPolicy> {
          PrivacyPolicyScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Login> {
          LoginScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.Register> {
          RegisterScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }

        composable<NavRoute.ForgotPassword> {
          ForgotPasswordScreen(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
          )
        }
      }
    }
  }
}
