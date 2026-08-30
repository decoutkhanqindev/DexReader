package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics.StatisticsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.components.StatisticsContent

@Composable
fun StatisticsScreen(
  viewModel: StatisticsViewModel,
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SideEffect(currentUser?.id) {
    viewModel.updateUserId(currentUser?.id)
  }

  BaseDetailsScreen(
    title = stringResource(R.string.statistics_menu_item),
    modifier = modifier,
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
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
