package com.decoutkhanqindev.dexreader.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent

@Composable
fun SettingsScreen(
  settingsViewModel: SettingsViewModel,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  onNavigateToLanguageScreen: () -> Unit,
  onNavigateToPrivacyScreen: () -> Unit,
) {
  val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

  BaseDetailsScreen(
    title = stringResource(R.string.settings_menu_item),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = onNavigateBack,
  ) {
    SettingsContent(
      isDarkTheme = uiState.appliedThemeOption == ThemeModeValue.DARK,
      modifier = Modifier.fillMaxSize(),
      onDarkThemeChange = { isDark ->
        settingsViewModel.updateThemeOption(if (isDark) ThemeModeValue.DARK else ThemeModeValue.LIGHT)
        settingsViewModel.saveThemeOption()
      },
      onNavigateToLanguageScreen = onNavigateToLanguageScreen,
      onNavigateToPrivacyScreen = onNavigateToPrivacyScreen,
    )
  }
}
