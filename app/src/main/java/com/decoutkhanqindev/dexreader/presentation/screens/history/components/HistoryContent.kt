package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.RemoveFromHistoryUiState

@Composable
fun HistoryContent(
  historyUiState: BasePaginationUiState<ReadingHistory>,
  removeFromHistoryUiState: RemoveFromHistoryUiState,
  onContinueReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onMangaDetailsClick: (String) -> Unit,
  onUpdateRemoveReadingHistoryId: (String) -> Unit,
  onRemoveFromHistory: () -> Unit,
  onRetryRemoveFromHistory: () -> Unit,
  onObserveHistoryNextPage: () -> Unit,
  onRetryObserveHistoryNextPage: () -> Unit,
  onRetryObserveHistoryFirstPage: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedMangaId by rememberSaveable { mutableStateOf<String?>(null) }
  var selectedChapterId by rememberSaveable { mutableStateOf<String?>(null) }
  var selectedLastReadPage by rememberSaveable { mutableStateOf<Int?>(null) }
  var isShowNavigateDialog by rememberSaveable { mutableStateOf(false) }
  var isShowRemoveFromHistoryDialog by rememberSaveable { mutableStateOf(false) }
  var isShowRemoveFromHistoryErrorDialog by rememberSaveable { mutableStateOf(true) }
  var isShowRemoveFromHistorySuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowHistoryErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (historyUiState) {
    BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    BasePaginationUiState.FirstPageError -> {
      if (isShowHistoryErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowHistoryErrorDialog = false },
          onConfirmClick = {
            isShowHistoryErrorDialog = false
            onRetryObserveHistoryFirstPage()
          },
        )
      }
    }

    is BasePaginationUiState.Content -> {
      val readingHistoryList = historyUiState.currentList
      val nextPageState = historyUiState.nextPageState

      if (readingHistoryList.isEmpty()) {
        IdleScreen(
          message = stringResource(R.string.you_have_no_reading_history_here),
          modifier = Modifier.fillMaxSize()
        )
      } else {
        ReadingHistoryList(
          readingHistoryList = readingHistoryList,
          historyNextPageState = nextPageState,
          onSelectedReadingHistory = { mangaId, chapterId, lastReadPage ->
            isShowNavigateDialog = true
            selectedMangaId = mangaId
            selectedChapterId = chapterId
            selectedLastReadPage = lastReadPage
          },
          onRemoveFromHistory = { readingHistoryId ->
            isShowRemoveFromHistoryDialog = true
            onUpdateRemoveReadingHistoryId(readingHistoryId)
          },
          onObserveHistoryNextPage = onObserveHistoryNextPage,
          onRetryObserveHistoryNextPage = onRetryObserveHistoryNextPage,
          modifier =
            if (removeFromHistoryUiState.isLoading) modifier.blur(8.dp)
            else modifier
        )
      }

      when {
        removeFromHistoryUiState.isLoading -> LoadingScreen(modifier = modifier)

        removeFromHistoryUiState.isError -> {
          if (isShowRemoveFromHistoryErrorDialog) {
            NotificationDialog(
              title = stringResource(R.string.removed_from_history_failed),
              onDismissClick = { isShowRemoveFromHistoryErrorDialog = false },
              onConfirmClick = {
                isShowRemoveFromHistoryErrorDialog = false
                onRetryRemoveFromHistory()
              },
            )
          }
        }

        removeFromHistoryUiState.isSuccess -> {
          if (isShowRemoveFromHistorySuccessDialog) {
            NotificationDialog(
              icon = Icons.Default.Done,
              title = stringResource(R.string.you_have_removed_from_history_successfully),
              isEnableDismiss = false,
              confirm = stringResource(R.string.ok),
              onConfirmClick = { isShowRemoveFromHistorySuccessDialog = false },
            )
          }
        }
      }

      if (isShowRemoveFromHistoryDialog && removeFromHistoryUiState.readingHistoryId != null) {
        NotificationDialog(
          title = stringResource(R.string.are_you_sure_you_want_to_remove_it_from_your_history),
          onDismissClick = { isShowRemoveFromHistoryDialog = false },
          confirm = stringResource(R.string.remove),
          onConfirmClick = {
            isShowRemoveFromHistoryDialog = false
            onRemoveFromHistory()
          },
        )
      }

      if (isShowNavigateDialog &&
        selectedMangaId != null &&
        selectedChapterId != null &&
        selectedLastReadPage != null
      ) {
        NotificationDialog(
          title = stringResource(R.string.view_details_or_continue),
          dismiss = stringResource(R.string.continue_reading),
          onDismissClick = {
            isShowNavigateDialog = false
            onContinueReadingClick(
              selectedChapterId!!,
              selectedLastReadPage!!,
              selectedMangaId!!
            )
          },
          confirm = stringResource(R.string.manga_details),
          onConfirmClick = {
            isShowNavigateDialog = false
            onMangaDetailsClick(selectedMangaId!!)
          },
        )
      }
    }
  }
}