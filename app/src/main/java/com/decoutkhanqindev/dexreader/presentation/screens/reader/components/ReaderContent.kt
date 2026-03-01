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
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.reader.ChapterPagesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages.ChapterPagesSection

@Composable
fun ReaderContent(
  chapterPageUiState: ChapterPagesUiState,
  onUpdateChapterPage: (Int) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (chapterPageUiState) {
    ChapterPagesUiState.Loading -> LoadingScreen(modifier = modifier)

    ChapterPagesUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
        )
      }
    }

    is ChapterPagesUiState.Success -> {
      val chapterPages = chapterPageUiState.chapterPages.pages
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        } else {
          ChapterPagesSection(
            chapterPages = chapterPages,
            currentPage = currentPage,
            totalPages = totalPages,
            onUpdateChapterPage = onUpdateChapterPage,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }
}



