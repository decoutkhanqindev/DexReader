@file:JvmName("MangaChaptersTitleKt")

package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaChaptersUiState


@Composable
fun MangaChaptersSection(
  mangaChaptersUiState: MangaChaptersUiState,
  chapterLanguage: String,
  chapterLanguageList: List<String>,
  onSelectedLanguage: (String) -> Unit,
  onSelectedChapter: (String) -> Unit,
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
      MangaChaptersUiState.FirstPageLoading -> ListLoadingIndicator(modifier = Modifier.fillMaxSize())
      MangaChaptersUiState.FirstPageError -> LoadPageErrorMessage(
        message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
        onRetry = onRetry,
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 4.dp)
      )

      is MangaChaptersUiState.Content -> {
        val chapterList = mangaChaptersUiState.chapterList
        val chapterListNextPageState = mangaChaptersUiState.nextPageState

        MangaChapterList(
          chapterList = chapterList,
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
