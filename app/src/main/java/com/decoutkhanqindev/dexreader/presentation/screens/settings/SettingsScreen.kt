package com.decoutkhanqindev.dexreader.presentation.screens.settings


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent

@Composable
fun SettingsScreen(
  viewModel: SettingsViewModel = hiltViewModel(),
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
      onThemeOptionClick = viewModel::updateThemeOption,
      onSaveThemeOption = viewModel::saveThemeOption,
      onRetry = viewModel::retry,
    )
  }
}