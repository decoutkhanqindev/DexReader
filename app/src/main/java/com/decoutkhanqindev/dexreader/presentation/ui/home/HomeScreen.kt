package com.decoutkhanqindev.dexreader.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.home.components.HomeContent
import com.decoutkhanqindev.dexreader.presentation.ui.home.components.HomeTopBar

@Composable
fun HomeScreen(
  onMenuClick: () -> Unit,
  onSearchClick: () -> Unit,
  onSelectedManga: (String) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    modifier = modifier,
    topBar = {
      HomeTopBar(
        title = stringResource(R.string.app_name),
        onMenuClick = onMenuClick,
        onSearchClick = onSearchClick,
        modifier = Modifier.fillMaxWidth()
      )
    },
    content = { innerPadding ->
      HomeContent(
        uiState = uiState,
        onSelectedManga = onSelectedManga,
        onRetry = { viewModel.retry() },
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize(),
      )
    },
  )
}


