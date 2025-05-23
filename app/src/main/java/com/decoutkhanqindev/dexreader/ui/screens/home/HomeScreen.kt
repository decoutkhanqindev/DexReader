package com.decoutkhanqindev.dexreader.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.decoutkhanqindev.dexreader.ui.components.ErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.HomeTopBar
import com.decoutkhanqindev.dexreader.ui.components.HorizontalMangaList
import com.decoutkhanqindev.dexreader.ui.components.LoadingScreen

@Composable
fun HomeScreen(
  onMenuClick: () -> Unit,
  onSearchClick: () -> Unit,
  onMangaClick: (String) -> Unit,
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
    content = { paddingValues ->
      HomeContent(
        uiState = uiState,
        onMangaClick = onMangaClick,
        onRetryClick = { viewModel.loadData() },
        modifier = Modifier.padding(paddingValues),
      )
    },
  )
}

@Composable
fun HomeContent(
  uiState: HomeUiState,
  onMangaClick: (String) -> Unit,
  onRetryClick: () -> Unit,
  modifier: Modifier,
) {
  when (uiState) {
    HomeUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())

    is HomeUiState.Success -> {
      LazyColumn(modifier = modifier) {
        item {
          MangaList(
            title = stringResource(R.string.latest_uploads),
            mangaList = uiState.latestUploadedMangaList,
            onMangaClick = { onMangaClick(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
        item {
          MangaList(
            title = stringResource(R.string.trending),
            mangaList = uiState.trendingMangaList,
            onMangaClick = { onMangaClick(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
        item {
          MangaList(
            title = stringResource(R.string.new_releases),
            mangaList = uiState.newReleaseMangaList,
            onMangaClick = { onMangaClick(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
        item {
          MangaList(
            title = stringResource(R.string.completed),
            mangaList = uiState.completedMangaList,
            onMangaClick = { onMangaClick(it.id) },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }

    HomeUiState.Error -> ErrorScreen(
      errorMessage = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetryClick = onRetryClick,
      modifier = modifier.fillMaxSize()
    )
  }
}

@Composable
private fun MangaList(
  title: String,
  mangaList: List<Manga>,
  onMangaClick: (Manga) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      HorizontalMangaList(
        mangaList = mangaList,
        onMangaClick = onMangaClick,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

