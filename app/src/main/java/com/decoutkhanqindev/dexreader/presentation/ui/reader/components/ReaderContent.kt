package com.decoutkhanqindev.dexreader.presentation.ui.reader.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.reader.ChapterPagesUiState

@Composable
fun ReaderContent(
  chapterPageUiState: ChapterPagesUiState,
  onUpdateChapterPage: (Int) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (chapterPageUiState) {
    ChapterPagesUiState.Loading -> LoadingScreen(modifier = modifier)
    ChapterPagesUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is ChapterPagesUiState.Success -> {
      val chapterPages = chapterPageUiState.chapterPages.pageUrls
      val currentPage = chapterPageUiState.currentChapterPage
      val totalPages = chapterPageUiState.chapterPages.totalPages

      if (chapterPages.isEmpty()) {
        Text(
          text = stringResource(R.string.no_chapter_pages_available),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      } else {
        ChapterPageSection(
          chapterPages = chapterPages,
          currentPage = currentPage,
          totalPages = totalPages,
          onUpdateChapterPage = onUpdateChapterPage,
          modifier = modifier,
        )
      }
    }
  }
}



