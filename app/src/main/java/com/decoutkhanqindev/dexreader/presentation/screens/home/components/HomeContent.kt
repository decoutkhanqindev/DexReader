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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeUiState
import kotlinx.collections.immutable.persistentListOf

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
  }
}

private val previewMangaList = persistentListOf(
  MangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    description = "A pirate adventure.",
    author = "Eiichiro Oda",
    artist = "Eiichiro Oda",
    categories = persistentListOf(CategoryModel(id = "g1", title = "Action")),
    status = MangaStatusValue.ON_GOING,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1997",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "1110",
    updatedAt = "2024-01-01",
  ),
  MangaModel(
    id = "2",
    title = "Naruto",
    coverUrl = "",
    description = "A ninja story.",
    author = "Masashi Kishimoto",
    artist = "Masashi Kishimoto",
    categories = persistentListOf(CategoryModel(id = "g2", title = "Adventure")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1999",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "700",
    updatedAt = "2014-11-10",
  ),
  MangaModel(
    id = "3",
    title = "Attack on Titan",
    coverUrl = "",
    description = "Humanity vs Titans.",
    author = "Hajime Isayama",
    artist = "Hajime Isayama",
    categories = persistentListOf(CategoryModel(id = "g3", title = "Action")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "2009",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "139",
    updatedAt = "2021-04-09",
  ),
)

@Preview
@Composable
private fun HomeContentLoadingPreview() {
  HomeContent(
    uiState = HomeUiState.Loading,
    modifier = Modifier.fillMaxSize(),
    onItemClick = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun HomeContentErrorPreview() {
  HomeContent(
    uiState = HomeUiState.Error(FeatureError.NetworkUnavailable),
    modifier = Modifier.fillMaxSize(),
    onItemClick = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun HomeContentSuccessPreview() {
  HomeContent(
    uiState = HomeUiState.Success(
      latestUpdatesMangaList = previewMangaList,
      trendingMangaList = previewMangaList,
      newReleaseMangaList = previewMangaList,
      topRatedMangaList = previewMangaList,
    ),
    modifier = Modifier.fillMaxSize(),
    onItemClick = {},
    onRetry = {}
  )
}