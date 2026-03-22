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
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.components.HistoryContent

@Composable
fun HistoryScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuItemValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
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
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    selectedMenuItem = MenuItemValue.HISTORY,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    content = {
      if (isUserLoggedIn) {
        HistoryContent(
          historyUiState = historyUiState,
          removeFromHistoryUiState = removeFromHistoryUiState,
          onContinueReadingClick = onNavigateToReaderScreen,
          onMangaDetailsClick = onNavigateToMangaDetailScreen,
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