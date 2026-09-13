package com.decoutkhanqindev.dexreader.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.prefs.PrefsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun SettingsScreen(
  navController: NavHostController,
  prefsViewModel: PrefsViewModel,
  modifier: Modifier = Modifier,
) {
  val uiState by prefsViewModel.data.collectAsStateWithLifecycle()

  BaseDetailsScreen(
    title = stringResource(R.string.settings_menu_item),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = { navController.navigateBack() },
  ) {
    SettingsContent(
      isDarkTheme = uiState.appliedThemeOption == ThemeModeValue.DARK,
      modifier = Modifier.fillMaxSize(),
      onDarkThemeChange = { isDark ->
        prefsViewModel.updateThemeOption(if (isDark) ThemeModeValue.DARK else ThemeModeValue.LIGHT)
        prefsViewModel.saveThemeOption()
      },
      onNavigateToLanguageScreen = { navController.navigateTo(NavRoute.LanguageSetting) },
      onNavigateToPrivacyScreen = { navController.navigateTo(NavRoute.PrivacyPolicy) },
    )
  }
}
