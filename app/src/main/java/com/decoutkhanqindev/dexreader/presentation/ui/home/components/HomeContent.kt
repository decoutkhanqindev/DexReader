package com.decoutkhanqindev.dexreader.presentation.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.home.HomeUiState

@Composable
fun HomeContent(
  uiState: HomeUiState,
  onSelectedManga: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier,
) {
  when (uiState) {
    HomeUiState.Loading -> LoadingScreen(modifier = modifier)

    is HomeUiState.Success -> {
      LazyColumn(modifier = modifier) {
        item {
          MangaListSection(
            title = stringResource(R.string.latest_update),
            mangaList = uiState.latestUpdateMangaList,
            onSelectedManga = { onSelectedManga(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        item {
          MangaListSection(
            title = stringResource(R.string.trending),
            mangaList = uiState.trendingMangaList,
            onSelectedManga = { onSelectedManga(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        item {
          MangaListSection(
            title = stringResource(R.string.new_releases),
            mangaList = uiState.newReleaseMangaList,
            onSelectedManga = { onSelectedManga(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        item {
          MangaListSection(
            title = stringResource(R.string.completed),
            mangaList = uiState.completedMangaList,
            onSelectedManga = { onSelectedManga(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    HomeUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )
  }
}
