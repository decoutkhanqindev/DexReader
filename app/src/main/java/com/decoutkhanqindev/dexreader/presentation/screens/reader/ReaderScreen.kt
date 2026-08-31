package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.AppTopBar
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
  var isFullScreen by remember { mutableStateOf(false) }
  var isShowResetConfirmDialog by remember { mutableStateOf(false) }
  var isShowResetSuccessDialog by remember { mutableStateOf(false) }
  var isShowResetErrorDialog by remember { mutableStateOf(false) }

  BackHandler { onNavigateBack() }

  SideEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(userId = currentUser.id)
    else viewModel.updateUserId(userId = null)
  }

  SideEffect(resetProgressUiState.isSuccess) {
    if (resetProgressUiState.isSuccess) isShowResetSuccessDialog = true
  }

  SideEffect(resetProgressUiState.isError) {
    if (resetProgressUiState.isError) isShowResetErrorDialog = true
  }

  Scaffold(
    topBar = {
      AnimatedVisibility(
        visible = !isFullScreen,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        AppTopBar(
          leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
          onLeftClick = onNavigateBack,
          centerContent = {
            Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(4.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = stringResource(
                  R.string.volume_chapter,
                  chapterDetailsUiState.volume,
                  chapterDetailsUiState.chapterNumber
                ),
                style = MaterialTheme.typography.titleMedium,
              )
              if (chapterDetailsUiState.title.isNotEmpty()) {
                Text(
                  text = chapterDetailsUiState.title,
                  fontStyle = FontStyle.Italic,
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.labelMedium,
                )
              }
            }
          },
          rightIcon = Icons.Default.RestartAlt,
          onRightClick = { isShowResetConfirmDialog = true },
          modifier = Modifier.fillMaxWidth(),
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
          lastReadPage = currentPage,
          pageCount = totalPages,
          canNavigatePrevious = chapterNavUiState.canNavigatePrevious,
          canNavigateNext = chapterNavUiState.canNavigateNext,
          modifier = Modifier.fillMaxWidth(),
          onNavigatePrevious = { viewModel.navigateToPreviousChapter() },
          onNavigateNext = { viewModel.navigateToNextChapter() },
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
      resetProgressUiState = resetProgressUiState,
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      onUpdateChapterPage = { viewModel.updateChapterPage(it) },
      onRetry = { viewModel.retry() },
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
