package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeUiState

@Composable
fun HomeContent(
  uiState: HomeUiState,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    HomeUiState.Loading -> LoadingScreen(modifier = modifier)

    is HomeUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(uiState.error.messageRes),
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
          onDismissClick = { isShowErrorDialog = false },
        )
      }
    }

    is HomeUiState.Success -> {
      LazyColumn(modifier = modifier) {
        item {
          MangaListSection(
            title = stringResource(R.string.latest_update),
            items = uiState.latestUpdatesMangaList,
            modifier = Modifier.fillMaxWidth(),
          ) { onItemClick(it.id) }
        }

        item {
          MangaListSection(
            title = stringResource(R.string.trending),
            items = uiState.trendingMangaList,
            modifier = Modifier.fillMaxWidth(),
          ) { onItemClick(it.id) }
        }

        item {
          MangaListSection(
            title = stringResource(R.string.new_releases),
            items = uiState.newReleaseMangaList,
            modifier = Modifier.fillMaxWidth(),
          ) { onItemClick(it.id) }
        }

        item {
          MangaListSection(
            title = stringResource(R.string.top_rated),
            items = uiState.topRatedMangaList,
            modifier = Modifier.fillMaxWidth(),
          ) { onItemClick(it.id) }
        }
      }
    }
  }
}