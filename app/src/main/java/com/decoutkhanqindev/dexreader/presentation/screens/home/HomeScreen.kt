package com.decoutkhanqindev.dexreader.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.components.HomeContent

@Composable
fun HomeScreen(
  viewModel: HomeViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.HOME,
    modifier = modifier,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    HomeContent(
      uiState = uiState,
      modifier = Modifier.fillMaxSize(),
      onItemClick = onNavigateToMangaDetailScreen,
      onRetry = viewModel::retry,
    )
  }
}
