package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaChaptersUiState
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaDetailsUiState
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.actions.ActionButtonsSection
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters.MangaChaptersSection
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.info.MangaInfoSection
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary.MangaSummarySection
import kotlinx.coroutines.launch

@Composable
fun MangaDetailsContent(
  mangaDetailsUiState: MangaDetailsUiState,
  mangaChaptersUiState: MangaChaptersUiState,
  canRead: Boolean,
  isReading: Boolean,
  onReadingClick: () -> Unit,
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  chapterLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  onSelectedCategory: (String, String) -> Unit,
  onSelectedChapter: (String) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListFirstPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible = (mangaChaptersUiState is MangaChaptersUiState.Content)
      && mangaChaptersUiState.chapterList.size > 15
      && lazyListState.firstVisibleItemScrollOffset > 0

  Box(modifier = modifier) {
    when (mangaDetailsUiState) {
      MangaDetailsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      MangaDetailsUiState.Error -> ErrorScreen(
        message = stringResource(R.string.oops_something_went_wrong_please_try_again),
        onRetry = onRetry,
        modifier = Modifier.fillMaxSize()
      )

      is MangaDetailsUiState.Success -> {
        val manga = mangaDetailsUiState.manga
        val mangaCoverUrl = manga.coverUrl
        val chapterLanguageList = manga.availableTranslatedLanguages
        val lastChapter = manga.lastChapter

        MangaDetailsBackground(
          imageUrl = mangaCoverUrl,
          modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
          state = lazyListState,
          modifier = Modifier
            .fillMaxSize()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.surface.copy(0.8f),
                  MaterialTheme.colorScheme.surface.copy(1f)
                )
              )
            )
        ) {
          item {
            MangaInfoSection(
              manga = manga,
              modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(top = 8.dp, bottom = 16.dp)
            )
          }

          item {
            ActionButtonsSection(
              canRead = canRead,
              isReading = isReading,
              onReadingClick = onReadingClick,
              isFavorite = isFavorite,
              onFavoriteClick = onFavoriteClick,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(bottom = 16.dp),
            )
          }

          item {
            MangaSummarySection(
              manga = manga,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            )
          }

          item {
            MangaChaptersSection(
              mangaChaptersUiState = mangaChaptersUiState,
              lastChapter = lastChapter,
              chapterLanguage = chapterLanguage,
              chapterLanguageList = chapterLanguageList,
              onSelectedLanguage = onSelectedLanguage,
              onSelectedChapter = onSelectedChapter,
              onFetchChapterListNextPage = onFetchChapterListNextPage,
              onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
              onRetry = onRetryFetchChapterListFirstPage,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    AnimatedVisibility(
      visible = isMoveToTopButtonVisible,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      MoveToTopButton(
        onClick = {
          coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
          }
        },
        modifier = Modifier.size(56.dp)
      )
    }
  }
}

