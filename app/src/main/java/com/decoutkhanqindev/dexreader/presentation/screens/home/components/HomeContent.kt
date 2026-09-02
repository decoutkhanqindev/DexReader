package com.decoutkhanqindev.dexreader.presentation.screens.home.components

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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaSectionValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section.MangaSectionUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
  mangaSectionUiState: MangaSectionUiState,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
  onMoreClick: (sectionTitle: String, sortCriteria: MangaSortCriteriaValue) -> Unit,
  onRetry: () -> Unit,
  onRefresh: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  SideEffect(mangaSectionUiState) {
    if (mangaSectionUiState is MangaSectionUiState.Error) isShowErrorDialog = true
  }

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = false,
    onRefresh = onRefresh,
    modifier = modifier
  ) {
    when (mangaSectionUiState) {
      MangaSectionUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is MangaSectionUiState.Success -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (mangaSectionUiState.bannerList.isNotEmpty()) {
            MangaBanner(
              items = mangaSectionUiState.bannerList,
              modifier = Modifier
                .fillMaxWidth()
                .height(365.dp)
                .padding(top = 8.dp),
              onItemClick = onItemClick
            )
          }

          Column(
            modifier = Modifier.padding(top = 16.dp, bottom = 82.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
          ) {
            MangaSectionValue.entries.forEach { section ->
              MangaListSection(
                section = section,
                items = mangaSectionUiState.mainSections[section] ?: persistentListOf(),
                modifier = Modifier.fillMaxWidth(),
                onItemClick = onItemClick,
                onMoreClick = onMoreClick,
              )
            }
          }
        }
      }

      is MangaSectionUiState.Error -> {
        if (isShowErrorDialog) {
          AlertDialog(
            title = stringResource(mangaSectionUiState.error.messageRes),
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
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
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
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
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
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
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
      mangaSectionUiState = MangaSectionUiState.Loading,
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      onMoreClick = { _, _ -> },
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
      mangaSectionUiState = MangaSectionUiState.Error(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      onMoreClick = { _, _ -> },
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
      mangaSectionUiState = MangaSectionUiState.Success(
        bannerList = previewMangaList,
        mainSections = persistentMapOf(
          MangaSectionValue.TRENDING to previewMangaList,
          MangaSectionValue.LATEST_UPDATE to previewMangaList,
          MangaSectionValue.NEW_RELEASE to previewMangaList,
          MangaSectionValue.TOP_RATED to previewMangaList,
        ),
      ),
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      onMoreClick = { _, _ -> },
      onRetry = {},
      onRefresh = {}
    )
  }
}
