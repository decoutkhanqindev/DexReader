package com.decoutkhanqindev.dexreader.presentation.screens.favorites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.components.FavoritesContent

@Composable
fun FavoritesScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuItemValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  viewModel: FavoritesViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuItemValue.FAVORITES,
    modifier = modifier,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    if (isUserLoggedIn) {
        FavoritesContent(
          uiState = uiState,
          onSelectedManga = onNavigateToMangaDetailScreen,
          onObserveFavoriteMangaListNextPage = viewModel::observeFavoritesNextPage,
          onRetryObserveFavoriteMangaListNextPage = viewModel::retryObserveFavoritesNextPage,
          onRetry = viewModel::retry,
          modifier = Modifier.fillMaxSize()
        )
      } else {
        IdleScreen(
          message = stringResource(R.string.please_sign_in_to_view_your_favorites),
          modifier = Modifier.fillMaxSize()
        )
      }
  }
}