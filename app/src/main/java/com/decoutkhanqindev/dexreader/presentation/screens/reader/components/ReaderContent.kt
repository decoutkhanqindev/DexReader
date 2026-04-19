package com.decoutkhanqindev.dexreader.presentation.screens.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.decoutkhanqindev.dexreader.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterPagesModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ChapterPagesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages.ChapterPagesSection
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReaderContent(
  chapterPageUiState: ChapterPagesUiState,
  modifier: Modifier = Modifier,
  onUpdateChapterPage: (Int) -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (chapterPageUiState) {
    ChapterPagesUiState.Loading -> LoadingScreen(modifier = modifier)

    is ChapterPagesUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(chapterPageUiState.error.messageRes),
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
          onDismissClick = { isShowErrorDialog = false },
        )
      }
    }

    is ChapterPagesUiState.Success -> {
      val chapterPages = chapterPageUiState.chapterPages.pageImageUrls
      val currentPage = chapterPageUiState.currentChapterPage
      val totalPages = chapterPageUiState.chapterPages.totalPages

      Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (chapterPages.isEmpty()) {
          Text(
            text = stringResource(R.string.no_chapter_pages_available),
            modifier = Modifier.fillMaxWidth(),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
          )
        } else {
          ChapterPagesSection(
            chapterPages = chapterPages,
            currentPage = currentPage,
            totalPages = totalPages,
            modifier = Modifier.fillMaxSize(),
          ) { onUpdateChapterPage(it) }
        }
      }
    }
  }
}

@Preview
@Composable
private fun ReaderContentLoadingPreview() {
  ReaderContent(
    chapterPageUiState = ChapterPagesUiState.Loading,
    modifier = Modifier.fillMaxSize(),
    onUpdateChapterPage = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ReaderContentErrorPreview() {
  ReaderContent(
    chapterPageUiState = ChapterPagesUiState.Error(FeatureError.NetworkUnavailable),
    modifier = Modifier.fillMaxSize(),
    onUpdateChapterPage = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ReaderContentEmptyPagesPreview() {
  ReaderContent(
    chapterPageUiState = ChapterPagesUiState.Success(
      currentChapterPage = 1,
      chapterPages = ChapterPagesModel(
        chapterId = "c-001",
        pageImageUrls = persistentListOf(),
        totalPages = 0,
      )
    ),
    modifier = Modifier.fillMaxSize(),
    onUpdateChapterPage = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ReaderContentWithPagesPreview() {
  ReaderContent(
    chapterPageUiState = ChapterPagesUiState.Success(
      currentChapterPage = 1,
      chapterPages = ChapterPagesModel(
        chapterId = "c-001",
        pageImageUrls = persistentListOf("", "", "", ""),
        totalPages = 4,
      )
    ),
    modifier = Modifier.fillMaxSize(),
    onUpdateChapterPage = {},
    onRetry = {}
  )
}



