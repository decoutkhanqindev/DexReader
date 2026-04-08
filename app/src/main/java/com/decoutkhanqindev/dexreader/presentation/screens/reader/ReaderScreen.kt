package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.DetailsTopBar
import com.decoutkhanqindev.dexreader.presentation.screens.reader.components.ReaderContent
import com.decoutkhanqindev.dexreader.presentation.screens.reader.components.actions.NavigateChapterBottomBar
import com.decoutkhanqindev.dexreader.presentation.screens.reader.components.actions.ZoomPageButton

@Composable
fun ReaderScreen(
  viewModel: ReaderViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
) {
  val chapterDetailsUiState by viewModel.chapterDetailsUiState.collectAsStateWithLifecycle()
  val chapterPagesUiState by viewModel.chapterPagesUiState.collectAsStateWithLifecycle()
  val chapterNavUiState by viewModel.chapterNavUiState.collectAsStateWithLifecycle()
  val (currentPage, totalPages) = remember(chapterPagesUiState) {
    when (chapterPagesUiState) {
      is ChapterPagesUiState.Success -> {
        val s = chapterPagesUiState as ChapterPagesUiState.Success
        s.currentChapterPage.toString() to s.chapterPages.totalPages.toString()
      }

      else -> "0" to "0"
    }
  }
  var isFullScreen by rememberSaveable { mutableStateOf(false) }

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  Scaffold(
    topBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        DetailsTopBar(
          title = stringResource(R.string.reader_title, currentPage, totalPages),
          isSearchEnabled = false,
          modifier = Modifier.fillMaxWidth(),
          onNavigateBack = onNavigateBack,
        )
      }
    },
    bottomBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        NavigateChapterBottomBar(
          volume = chapterDetailsUiState.volume,
          chapterNumber = chapterDetailsUiState.chapterNumber,
          title = chapterDetailsUiState.title,
          canNavigatePrevious = chapterNavUiState.canNavigatePrevious,
          canNavigateNext = chapterNavUiState.canNavigateNext,
          modifier = Modifier.fillMaxWidth(),
          onNavigatePrevious = viewModel::navigateToPreviousChapter,
          onNavigateNext = viewModel::navigateToNextChapter,
        )
      }
    },
    floatingActionButton = {
      ZoomPageButton(
        isFullScreen = isFullScreen,
        modifier = Modifier.size(56.dp),
      ) { isFullScreen = !isFullScreen }
    },
    modifier = modifier
  ) { innerPadding ->
      ReaderContent(
        chapterPageUiState = chapterPagesUiState,
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        onUpdateChapterPage = viewModel::updateChapterPage,
        onRetry = viewModel::retry,
      )
  }
}