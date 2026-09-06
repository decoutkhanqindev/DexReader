package com.decoutkhanqindev.dexreader.presentation.screens.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.history.HistoryViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.history.components.HistoryContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun HistoryScreen(
  navController: NavHostController,
  viewModel: HistoryViewModel,
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
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
    onNavigateBack = { navController.navigateBack() },
    onNavigateToSearchScreen = { navController.navigateTo(NavRoute.Search) }
  ) {
    if (isUserLoggedIn) {
      HistoryContent(
        historyUiState = historyUiState,
        removeFromHistoryUiState = removeFromHistoryUiState,
        onContinueReadingClick = { chapterId, lastReadPage, mangaId ->
          navController.navigateTo(NavRoute.Reader(chapterId, lastReadPage, mangaId))
        },
        onMangaDetailsClick = { mangaId ->
          navController.navigateTo(NavRoute.MangaDetails(mangaId))
        },
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