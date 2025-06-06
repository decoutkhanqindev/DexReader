package com.decoutkhanqindev.dexreader.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.ui.home.components.HomeContent

@Composable
fun HomeScreen(
  onMenuItemClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  onSelectedManga: (String) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val route by rememberSaveable {
    mutableStateOf(NavigationDestination.HomeDestination.route)
  }

  BaseScreen(
    title = stringResource(R.string.app_name),
    route = route,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {
      HomeContent(
        uiState = uiState,
        onSelectedManga = onSelectedManga,
        onRetry = { viewModel.retry() },
        modifier = Modifier.fillMaxSize(),
      )
    },
    modifier = modifier
  )
}
