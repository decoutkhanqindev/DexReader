package com.decoutkhanqindev.dexreader.presentation.screens.history

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
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.history.HistoryViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.history.components.HistoryContent

@Composable
fun HistoryScreen(
  viewModel: HistoryViewModel,
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val historyUiState by viewModel.historyUiState.collectAsStateWithLifecycle()
  val removeFromHistoryUiState by viewModel.removeFromHistoryUiState.collectAsStateWithLifecycle()

  SideEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  BaseDetailsScreen(
    title = stringResource(R.string.history_menu_item),
    modifier = modifier,
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    if (isUserLoggedIn) {
      HistoryContent(
        historyUiState = historyUiState,
        removeFromHistoryUiState = removeFromHistoryUiState,
        onContinueReadingClick = onNavigateToReaderScreen,
        onMangaDetailsClick = onNavigateToMangaDetailScreen,
        onUpdateRemoveReadingHistoryId = { viewModel.updateRemoveReadingHistoryId(it) },
        onRemoveFromHistory = { viewModel.removeFromHistory() },
        onRetryRemoveFromHistory = { viewModel.retryRemoveFromHistory() },
        onObserveHistoryNextPage = { viewModel.observeHistoryNextPage() },
        onRetryObserveHistoryNextPage = { viewModel.retryObserveHistoryNextPage() },
        onRetryObserveHistoryFirstPage = { viewModel.retryObserveHistoryFirstPage() },
        onRefresh = { viewModel.refresh() },
        modifier = Modifier.fillMaxSize()
      )
    } else {
      IdleScreen(
        message = stringResource(R.string.please_sign_in_to_view_your_history),
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}