package com.decoutkhanqindev.dexreader.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun SettingsScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val dataStoreManager = LocalDataStoreManager.current
  val isDark by dataStoreManager.isDark.collectAsStateWithLifecycle()

  BaseDetailsScreen(
    title = stringResource(R.string.settings_menu_item),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = { navController.navigateBack() },
  ) {
    SettingsContent(
      isDarkTheme = isDark ?: true,
      modifier = Modifier.fillMaxSize(),
      onDarkThemeChange = { dataStoreManager.saveIsDark(it) },
      onNavigateToLanguageScreen = { navController.navigateTo(NavRoute.LanguageSetting) },
      onNavigateToPrivacyScreen = { navController.navigateTo(NavRoute.PrivacyPolicy) },
    )
  }
}
