package com.decoutkhanqindev.dexreader.presentation.screens.history

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
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.components.HistoryContent
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue

@Composable
fun HistoryScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (MenuItemValue) -> Unit,
  onSearchClick: () -> Unit,
  onContinueReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onMangaDetailsClick: (String) -> Unit,
  viewModel: HistoryViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val historyUiState by viewModel.historyUiState.collectAsStateWithLifecycle()
  val removeFromHistoryUiState by viewModel.removeFromHistoryUiState.collectAsStateWithLifecycle()

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    selectedMenuItem = MenuItemValue.HISTORY,
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