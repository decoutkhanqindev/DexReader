package com.decoutkhanqindev.dexreader.presentation.ui.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.reader.components.ReaderBottomBar
import com.decoutkhanqindev.dexreader.presentation.ui.reader.components.ReaderContent
import com.decoutkhanqindev.dexreader.presentation.ui.reader.components.ReaderFloatingButton
import com.decoutkhanqindev.dexreader.presentation.ui.reader.components.ReaderTopBar

@Composable
fun ReaderScreen(
  onNavigateBack: () -> Unit,
  viewModel: ReaderViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val chapterDetailsUiState by viewModel.chapterDetailsUiState.collectAsStateWithLifecycle()
  val chapterPagesUiState by viewModel.chapterPagesUiState.collectAsStateWithLifecycle()
  val (currentPage, totalPages) = when (chapterPagesUiState) {
    is ChapterPagesUiState.Success -> {
      val successUiState = chapterPagesUiState as ChapterPagesUiState.Success
      successUiState.currentChapterPage.toString() to successUiState.chapterPages.totalPages.toString()
    }

    else -> "1" to "∞"
  }
  val canNavigatePrevious by viewModel.canNavigatePrevious.collectAsStateWithLifecycle()
  val canNavigateNext by viewModel.canNavigateNext.collectAsStateWithLifecycle()
  var isFullScreen by rememberSaveable { mutableStateOf(false) }


  Scaffold(
    topBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically(),
        exit = shrinkVertically()
      ) {
        ReaderTopBar(
          currentChapterPage = currentPage,
          totalChapterPages = totalPages,
          onNavigateBack = onNavigateBack,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    bottomBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically(),
        exit = shrinkVertically()
      ) {
        ReaderBottomBar(
          volume = chapterDetailsUiState.volume,
          chapterNumber = chapterDetailsUiState.chapterNumber,
          title = chapterDetailsUiState.title,
          canNavigatePrevious = canNavigatePrevious,
          canNavigateNext = canNavigateNext,
          onNavigatePrevious = { viewModel.navigateToPreviousChapter() },
          onNavigateNext = { viewModel.navigateToNextChapter() },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    floatingActionButton = {
      ReaderFloatingButton(
        isFullScreen = isFullScreen,
        onClick = { isFullScreen = !isFullScreen },
        modifier = Modifier.size(56.dp)
      )
    },
    content = { innerPadding ->
      ReaderContent(
        chapterPageUiState = chapterPagesUiState,
        onUpdateChapterPage = { viewModel.updateChapterPage(newChapterPage = it) },
        onRetry = { viewModel.retry() },
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      )
    },
    modifier = modifier
  )
}