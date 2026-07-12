package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.home.HomeUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
  uiState: HomeUiState,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
  onRetry: () -> Unit,
  onRefresh: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  LaunchedEffect(uiState) {
    if (uiState is HomeUiState.Error) isShowErrorDialog = true
  }

  ReportDrawnWhen { uiState is HomeUiState.Success }

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = false,
    onRefresh = onRefresh,
    modifier = modifier
  ) {
    when (uiState) {
      HomeUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is HomeUiState.Success -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "home_feed" }
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (uiState.bannerMangaList.isNotEmpty()) {
            MangaBanner(
              mangaList = uiState.bannerMangaList,
              modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
              onItemClick = onItemClick
            )
          }

          Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
          ) {
            MangaListSection(
              title = stringResource(R.string.trending),
              items = uiState.trendingMangaList,
              modifier = Modifier.fillMaxWidth(),
              onItemClick = onItemClick,
            )

            MangaListSection(
              title = stringResource(R.string.latest_update),
              items = uiState.latestUpdatesMangaList,
              modifier = Modifier.fillMaxWidth(),
              onItemClick = onItemClick,
            )

            MangaListSection(
              title = stringResource(R.string.new_releases),
              items = uiState.newReleaseMangaList,
              modifier = Modifier.fillMaxWidth(),
              onItemClick = onItemClick,
            )

            MangaListSection(
              title = stringResource(R.string.top_rated),
              items = uiState.topRatedMangaList,
              modifier = Modifier.fillMaxWidth(),
              onItemClick = onItemClick,
            )
          }
        }
      }

      is HomeUiState.Error -> {
        if (isShowErrorDialog) {
          AlertDialog(
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
    rating = "9.1",
    follows = "2.3M",
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
    rating = "8.7",
    follows = "1.8M",
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
    rating = "9.0",
    follows = "1.2M",
  ),
)

@Preview
@Composable
private fun HomeContentLoadingPreview() {
  DexReaderTheme {
    HomeContent(
      uiState = HomeUiState.Loading,
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      onRetry = {},
      onRefresh = {}
    )
  }
}

@Preview
@Composable
private fun HomeContentErrorPreview() {
  DexReaderTheme {
    HomeContent(
      uiState = HomeUiState.Error(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      onRetry = {},
      onRefresh = {}
    )
  }
}

@Preview
@Composable
private fun HomeContentSuccessPreview() {
  DexReaderTheme {
    HomeContent(
      uiState = HomeUiState.Success(
        latestUpdatesMangaList = previewMangaList,
        trendingMangaList = previewMangaList,
        newReleaseMangaList = previewMangaList,
        topRatedMangaList = previewMangaList,
      ), modifier = Modifier.fillMaxSize(), onItemClick = {}, onRetry = {}, onRefresh = {}
    )
  }
}
