package com.decoutkhanqindev.dexreader.presentation.screens.settings


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent

@Composable
fun SettingsScreen(
  viewModel: SettingsViewModel,
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.SETTINGS,
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen
  ) {
    SettingsContent(
      uiState = uiState,
      modifier = Modifier.fillMaxSize(),
      onThemeOptionClick = remember { viewModel::updateThemeOption },
      onSaveThemeOption = remember { viewModel::saveThemeOption },
      onCancelThemeOption = remember { viewModel::resetThemeOption },
      onRetry = remember { viewModel::retry },
    )
  }
}