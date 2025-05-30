package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.components.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.ui.components.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.components.states.LoadingScreen
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
  isReading: Boolean,
  onReadingClick: (String) -> Unit,
  isFavorite: Boolean,
  onFavoriteClick: (String) -> Unit,
  chapterLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onSelectedGenre: (String) -> Unit,
  onSelectedChapter: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  val verticalScrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()

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
        val chapterLanguageList = manga.availableTranslatedLanguages

        MangaDetailsBackground(
          imageUrl = manga.coverUrl,
          contentDesc = manga.title,
          modifier = Modifier.fillMaxSize()
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                  MaterialTheme.colorScheme.surface.copy(alpha = 1.5f),
                )
              )
            )
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(verticalScrollState),
          ) {
            MangaInfoSection(
              manga = manga,
              modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(bottom = 16.dp)
            )

            ActionButtonsSection(
              isReading = isReading,
              onReadingClick = onReadingClick,
              isFavorite = isFavorite,
              onFavoriteClick = onFavoriteClick,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(bottom = 16.dp),
            )

            MangaSummarySection(
              manga = manga,
              onSelectedGenre = onSelectedGenre,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            )

            MangaChaptersSection(
              mangaChaptersUiState = mangaChaptersUiState,
              chapterLanguage = chapterLanguage,
              chapterLanguageList = chapterLanguageList,
              onSelectedLanguage = onSelectedLanguage,
              onSelectedChapter = onSelectedChapter,
              onFetchChapterListNextPage = onFetchChapterListNextPage,
              onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    val isMoveToTopButtonVisible = mangaChaptersUiState is MangaChaptersUiState.Content &&
        mangaChaptersUiState.chapterList.size > 15
    AnimatedVisibility(
      visible = isMoveToTopButtonVisible,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      MoveToTopButton(
        onClick = {
          coroutineScope.launch {
            verticalScrollState.animateScrollTo(0)
          }
        },
        modifier = Modifier.size(56.dp)
      )
    }
  }
}

