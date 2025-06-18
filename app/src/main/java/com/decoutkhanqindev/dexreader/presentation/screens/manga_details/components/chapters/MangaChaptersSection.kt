package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage

@Composable
fun MangaChaptersSection(
  mangaChaptersUiState: BasePaginationUiState<Chapter>,
  readingHistoryList: List<ReadingHistory> = emptyList(),
  lastChapter: String,
  chapterLanguage: String,
  chapterLanguageList: List<String>,
  onSelectedLanguage: (String) -> Unit,
  onSelectedChapter: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    MangaChaptersHeader(
      selectedLanguage = chapterLanguage,
      languageList = chapterLanguageList,
      onSelectedLanguage = onSelectedLanguage,
      modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
        .padding(horizontal = 4.dp)
    )

    when (mangaChaptersUiState) {
      BasePaginationUiState.FirstPageLoading -> ListLoadingIndicator(modifier = Modifier.fillMaxSize())

      BasePaginationUiState.FirstPageError -> LoadPageErrorMessage(
        message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
        onRetry = onRetry,
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 4.dp)
      )

      is BasePaginationUiState.Content -> {
        val chapterList = mangaChaptersUiState.currentList
        val chapterListNextPageState = mangaChaptersUiState.nextPageState

        MangaChapterList(
          lastChapter = lastChapter,
          chapterList = chapterList,
          readingHistoryList = readingHistoryList,
          onSelectedChapter = onSelectedChapter,
          chapterListNextPageState = chapterListNextPageState,
          onFetchChapterListNextPage = onFetchChapterListNextPage,
          onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}