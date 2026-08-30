package com.decoutkhanqindev.dexreader.presentation.screens.favorites

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
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.favorites.FavoritesViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.components.FavoritesContent

@Composable
fun FavoritesScreen(
  viewModel: FavoritesViewModel,
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SideEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  BaseDetailsScreen(
    title = stringResource(R.string.favorite_menu_item),
    modifier = modifier,
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    if (isUserLoggedIn) {
      FavoritesContent(
        uiState = uiState,
        onSelectedManga = onNavigateToMangaDetailScreen,
        onObserveFavoriteMangaListNextPage = { viewModel.observeFavoritesNextPage() },
        onRetryObserveFavoriteMangaListNextPage = { viewModel.retryObserveFavoritesNextPage() },
        onRetry = { viewModel.retry() },
        onRefresh = { viewModel.refresh() },
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