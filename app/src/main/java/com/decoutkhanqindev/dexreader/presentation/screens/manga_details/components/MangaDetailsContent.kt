package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions.ActionButtonsSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters.MangaChaptersSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info.MangaInfoSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary.MangaSummarySection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun MangaDetailsContent(
  mangaDetailsUiState: MangaDetailsUiState,
  mangaChaptersUiState: BasePaginationUiState<ChapterModel>,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  isFavorite: Boolean,
  onFavoriteClick: () -> Unit,
  chapterLanguage: MangaLanguageValue,
  availableLanguages: ImmutableList<MangaLanguageValue>,
  onSelectedLanguage: (MangaLanguageValue) -> Unit,
  onSelectedCategory: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
  onSelectedChapter: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListFirstPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
  readingHistoryList: ImmutableList<ReadingHistoryModel> = persistentListOf(),
  startedChapterId: String? = null,
  continueChapter: ReadingHistoryModel? = null,
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible by remember(mangaChaptersUiState, lazyListState) {
    derivedStateOf {
      (mangaChaptersUiState is BasePaginationUiState.Content)
          && mangaChaptersUiState.currentList.size > 15
          && lazyListState.firstVisibleItemScrollOffset > 0
    }
  }
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  Box(modifier = modifier) {
    when (mangaDetailsUiState) {
      MangaDetailsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is MangaDetailsUiState.Error -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            title = stringResource(mangaDetailsUiState.error.messageRes),
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      is MangaDetailsUiState.Success -> {
        val manga = mangaDetailsUiState.manga
        val mangaCoverUrl = manga.coverUrl
        val latestChapter = manga.latestChapter

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
                colors = persistentListOf(
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
              onReadingClick = onReadingClick,
              isFavorite = isFavorite,
              onFavoriteClick = onFavoriteClick,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(bottom = 16.dp),
              startedChapterId = startedChapterId,
              mangaId = manga.id,
              continueChapter = continueChapter,
            )
          }

          item {
            MangaSummarySection(
              manga = manga,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            ) { categoryId, categoryTitle ->
              onSelectedCategory(categoryId, categoryTitle)
            }
          }

          item {
            MangaChaptersSection(
              mangaChaptersUiState = mangaChaptersUiState,
              latestChapter = latestChapter,
              chapterLanguage = chapterLanguage,
              chapterLanguageList = availableLanguages,
              readingHistoryList = readingHistoryList,
              modifier = Modifier.fillMaxWidth(),
              onSelectedLanguage = onSelectedLanguage,
              onSelectedChapter = onSelectedChapter,
              onFetchChapterListNextPage = onFetchChapterListNextPage,
              onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
              onRetry = onRetryFetchChapterListFirstPage,
            )
          }
        }
      }
    }

    AnimatedVisibility(
      visible = isMoveToTopButtonVisible,
      enter = scaleIn(),
      exit = scaleOut(),
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
