package com.decoutkhanqindev.dexreader.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.ui.components.bar.HomeTopBar
import com.decoutkhanqindev.dexreader.ui.components.content.HorizontalMangaList
import com.decoutkhanqindev.dexreader.ui.components.states.ErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.LoadingScreen

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

@Composable
private fun HomeContent(
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

@Composable
private fun MangaListSection(
  title: String,
  mangaList: List<Manga>,
  onSelectedManga: (Manga) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier.padding(start = 16.dp, top = 8.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    HorizontalMangaList(
      mangaList = mangaList,
      onSelectedManga = onSelectedManga,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

