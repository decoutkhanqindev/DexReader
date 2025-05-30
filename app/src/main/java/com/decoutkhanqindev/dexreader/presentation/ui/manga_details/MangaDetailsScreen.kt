package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.MangaDetailsContent
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.MangaDetailsTopBar
import com.decoutkhanqindev.dexreader.utils.toFullLanguageName
import com.decoutkhanqindev.dexreader.utils.toLanguageCode

@Composable
fun MangaDetailsScreen(
  onNavigateBack: () -> Unit,
  onReadingClick: (String) -> Unit,
  onSelectedGenre: (String) -> Unit,
  onSelectedChapter: (String) -> Unit,
  viewModel: MangaDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val mangaDetailsUiState by viewModel.mangaDetailsUiState.collectAsStateWithLifecycle()
  val mangaChaptersUiState by viewModel.mangaChaptersUiState.collectAsStateWithLifecycle()
  val chapterLanguage by viewModel.chapterLanguage.collectAsStateWithLifecycle()
  var isReading by rememberSaveable { mutableStateOf(false) }
  var isFavorite by rememberSaveable { mutableStateOf(true) }

  Scaffold(
    topBar = {
      MangaDetailsTopBar(
        title = stringResource(R.string.manga_details),
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxWidth()
      )
    },
    content = { innerPadding ->
      MangaDetailsContent(
        mangaDetailsUiState = mangaDetailsUiState,
        mangaChaptersUiState = mangaChaptersUiState,
        isReading = isReading,
        onReadingClick = onReadingClick,
        isFavorite = isFavorite,
        onFavoriteClick = {},
        chapterLanguage = chapterLanguage.toFullLanguageName(),
        onSelectedLanguage = { viewModel.updateChapterLanguage(it.toLanguageCode()) },
        onSelectedGenre = onSelectedGenre,
        onSelectedChapter = onSelectedChapter,
        onFetchChapterListNextPage = { viewModel.fetchChapterListNextPage() },
        onRetryFetchChapterListNextPage = { viewModel.retryFetchChapterListNextPage() },
        onRetryFetchChapterListFirstPage = { viewModel.retryFetchChapterListFirstPage() },
        onRetry = { viewModel.retry() },
        modifier = modifier
          .padding(innerPadding)
          .fillMaxSize()
      )
    }
  )
}























