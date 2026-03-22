package com.decoutkhanqindev.dexreader.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.components.HomeContent

@Composable
fun HomeScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (MenuItemValue) -> Unit,
  onSearchClick: () -> Unit,
  onSelectedManga: (String) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onNavigateToSignInScreen = onSignInClick,
    selectedMenuItem = MenuItemValue.HOME,
    onNavigateToMenuItemScreen = onMenuItemClick,
    onNavigateToSearchScreen = onSearchClick,
    content = {
      HomeContent(
        uiState = uiState,
        onSelectedManga = onSelectedManga,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize(),
      )
    },
    modifier = modifier
  )
}
