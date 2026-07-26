package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ReadingProgressBar
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
  val resetProgressUiState by viewModel.resetProgressUiState.collectAsStateWithLifecycle()
  val (currentPage, totalPages) = remember(chapterPagesUiState) {
    when (chapterPagesUiState) {
      is ChapterPagesUiState.Success -> {
        val s = chapterPagesUiState as ChapterPagesUiState.Success
        s.currentChapterPage to s.chapterPages.totalPages
      }

      else -> 0 to 0
    }
  }
  val canResetProgress by remember(isUserLoggedIn) {
    derivedStateOf { isUserLoggedIn && chapterPagesUiState is ChapterPagesUiState.Success }
  }
  var isFullScreen by remember { mutableStateOf(false) }
  var isShowResetConfirmDialog by remember { mutableStateOf(false) }
  var isShowResetSuccessDialog by remember { mutableStateOf(false) }
  var isShowResetErrorDialog by remember { mutableStateOf(false) }

  BackHandler { onNavigateBack() }

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  LaunchedEffect(resetProgressUiState.isSuccess) {
    if (resetProgressUiState.isSuccess) isShowResetSuccessDialog = true
  }

  LaunchedEffect(resetProgressUiState.isError) {
    if (resetProgressUiState.isError) isShowResetErrorDialog = true
  }

  Scaffold(
    topBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        DetailsTopBar(
          titleContent = {
            ReadingProgressBar(
              lastReadPage = currentPage,
              pageCount = totalPages,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            )
          },
          isSearchEnabled = false,
          modifier = Modifier.fillMaxWidth(),
          actionsContent = {
            if (canResetProgress) {
              IconButton(onClick = { isShowResetConfirmDialog = true }) {
                Icon(
                  imageVector = Icons.Default.RestartAlt,
                  contentDescription = stringResource(R.string.reset_chapter_progress)
                )
              }
            }
          },
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
          onNavigatePrevious = remember { viewModel::navigateToPreviousChapter },
          onNavigateNext = remember { viewModel::navigateToNextChapter },
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
      onUpdateChapterPage = remember { { viewModel.updateChapterPage(it) } },
      onRetry = remember { viewModel::retry },
    )
  }

  if (isShowResetConfirmDialog) {
    AlertDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_reset_this_chapter_s_progress),
      confirm = stringResource(R.string.reset),
      onConfirmClick = {
        isShowResetConfirmDialog = false
        viewModel.resetChapterProgress()
      },
      onDismissClick = { isShowResetConfirmDialog = false },
    )
  }

  if (isShowResetSuccessDialog) {
    AlertDialog(
      icon = Icons.Default.Done,
      title = stringResource(R.string.you_have_reset_this_chapter_s_progress_successfully),
      isEnableDismiss = false,
      onConfirmClick = { isShowResetSuccessDialog = false },
    )
  }

  if (isShowResetErrorDialog) {
    AlertDialog(
      title = stringResource(R.string.reset_chapter_progress_failed),
      onConfirmClick = {
        isShowResetErrorDialog = false
        viewModel.retryResetChapterProgress()
      },
      onDismissClick = { isShowResetErrorDialog = false },
    )
  }
}
