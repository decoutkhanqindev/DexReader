package com.decoutkhanqindev.dexreader.presentation.ui.history.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.history.HistoryUiState

@Composable
fun HistoryContent(
  uiState: HistoryUiState,
  onSelectedReadingHistory: (String) -> Unit,
  onRemoveFromHistory: (String) -> Unit,
  onObserveHistoryNextPage: () -> Unit,
  onRetryObserveHistoryNextPage: () -> Unit,
  retryObserveHistoryFirstPage: () -> Unit,
  modifier: Modifier = Modifier
) {
  var removeReadingHistoryId by rememberSaveable { mutableStateOf<String?>(null) }
  var isShowRemoveFromHistoryDialog by rememberSaveable { mutableStateOf(false) }
  var isShowHistoryErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    HistoryUiState.Idle -> Unit

    HistoryUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    HistoryUiState.FirstPageError -> {
      if (isShowHistoryErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowHistoryErrorDialog = false },
          onConfirmClick = {
            isShowHistoryErrorDialog = false
            retryObserveHistoryFirstPage()
          },
        )
      }
    }

    is HistoryUiState.Content -> {
      val readingHistoryList = uiState.readingHistoryList
      val nextPageState = uiState.nextPageState

      if (readingHistoryList.isEmpty()) {
        IdleScreen(
          message = stringResource(R.string.you_have_no_reading_history_here),
          modifier = Modifier.fillMaxSize()
        )
      } else {
        ReadingHistoryList(
          readingHistoryList = readingHistoryList,
          historyNextPageState = nextPageState,
          onSelectedReadingHistory = onSelectedReadingHistory,
          onRemoveFromHistory = {
            isShowRemoveFromHistoryDialog = true
            removeReadingHistoryId = it
          },
          onObserveHistoryNextPage = onObserveHistoryNextPage,
          onRetryObserveHistoryNextPage = onRetryObserveHistoryNextPage,
          modifier = modifier
        )
      }

      if (isShowRemoveFromHistoryDialog && removeReadingHistoryId != null) {
        NotificationDialog(
          title = stringResource(R.string.are_you_sure_you_want_to_remove_it_from_your_history),
          onDismissClick = {
            isShowRemoveFromHistoryDialog = false
            removeReadingHistoryId = null
          },
          confirm = stringResource(R.string.remove),
          onConfirmClick = {
            onRemoveFromHistory(removeReadingHistoryId!!)
            isShowRemoveFromHistoryDialog = false
            removeReadingHistoryId = null
          },
        )
      }
    }
  }
}