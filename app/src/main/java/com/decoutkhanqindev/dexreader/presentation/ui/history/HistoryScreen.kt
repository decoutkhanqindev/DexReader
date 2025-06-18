package com.decoutkhanqindev.dexreader.presentation.ui.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.ui.history.components.HistoryContent

@Composable
fun HistoryScreen(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  onContinueReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String
  ) -> Unit,
  onMangaDetailsClick: (String) -> Unit,
  viewModel: HistoryViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val historyUiState by viewModel.historyUiState.collectAsStateWithLifecycle()
  val removeFromHistoryUiState by viewModel.removeFromHistoryUiState.collectAsStateWithLifecycle()
  val route = NavDestination.HistoryDestination.route

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.reset()
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    title = stringResource(R.string.history_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {
      if (isUserLoggedIn) {
        HistoryContent(
          historyUiState = historyUiState,
          removeFromHistoryUiState = removeFromHistoryUiState,
          onContinueReadingClick = onContinueReadingClick,
          onMangaDetailsClick = onMangaDetailsClick,
          onUpdateRemoveReadingHistoryId = viewModel::updateRemoveReadingHistoryId,
          onRemoveFromHistory = viewModel::removeFromHistory,
          onRetryRemoveFromHistory = viewModel::retryRemoveFromHistory,
          onObserveHistoryNextPage = viewModel::observeHistoryNextPage,
          onRetryObserveHistoryNextPage = viewModel::retryObserveHistoryNextPage,
          onRetryObserveHistoryFirstPage = viewModel::retryObserveHistoryFirstPage,
          modifier = Modifier.fillMaxSize()
        )
      } else {
        IdleScreen(
          message = stringResource(R.string.please_sign_in_to_view_your_history),
          modifier = Modifier.fillMaxSize()
        )
      }
    },
    modifier = modifier
  )
}