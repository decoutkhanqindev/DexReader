package com.decoutkhanqindev.dexreader.presentation.screens.settings


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.components.SettingsContent

@Composable
fun SettingsScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (MenuItemValue) -> Unit,
  viewModel: SettingsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onNavigateToSignInScreen = onSignInClick,
    selectedMenuItem = MenuItemValue.SETTINGS,
    onNavigateToMenuItemScreen = onMenuItemClick,
    isSearchEnabled = false,
    content = {
      SettingsContent(
        uiState = uiState,
        onThemeOptionClick = viewModel::updateThemeOption,
        onSaveThemeOption = viewModel::saveThemeOption,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}