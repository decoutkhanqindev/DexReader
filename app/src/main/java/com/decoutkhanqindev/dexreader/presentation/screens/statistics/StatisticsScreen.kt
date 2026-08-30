package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.components.StatisticsContent

@Composable
fun StatisticsScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  viewModel: StatisticsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SideEffect(currentUser?.id) {
    viewModel.updateUserId(currentUser?.id)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.STATISTICS,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    modifier = modifier,
  ) {
    if (isUserLoggedIn) {
      StatisticsContent(
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
        onRetry = { viewModel.retry() },
        onRefresh = { viewModel.refresh() },
      )
    } else {
      IdleScreen(
        message = stringResource(R.string.please_sign_in_to_view_your_statistics),
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}
