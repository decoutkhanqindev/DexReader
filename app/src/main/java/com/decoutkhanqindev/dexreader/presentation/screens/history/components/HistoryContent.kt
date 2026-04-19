package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.history.RemoveFromHistoryUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HistoryContent(
  historyUiState: BasePaginationUiState<ReadingHistoryModel>,
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

  Box(modifier = modifier) {
    when (historyUiState) {
      BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is BasePaginationUiState.FirstPageError -> {
        if (isShowHistoryErrorDialog) {
          NotificationDialog(
            onConfirmClick = {
              isShowHistoryErrorDialog = false
              onRetryObserveHistoryFirstPage()
            },
            title = stringResource(historyUiState.error.messageRes),
            onDismissClick = { isShowHistoryErrorDialog = false },
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
              if (removeFromHistoryUiState.isLoading) {
                Modifier
                  .fillMaxSize()
                  .blur(8.dp)
              } else Modifier.fillMaxSize()
          )
        }

        when {
          removeFromHistoryUiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

          removeFromHistoryUiState.isError -> {
            if (isShowRemoveFromHistoryErrorDialog) {
              NotificationDialog(
                onConfirmClick = {
                  isShowRemoveFromHistoryErrorDialog = false
                  onRetryRemoveFromHistory()
                },
                title = stringResource(R.string.removed_from_history_failed),
                onDismissClick = { isShowRemoveFromHistoryErrorDialog = false },
              )
            }
          }

          removeFromHistoryUiState.isSuccess -> {
            if (isShowRemoveFromHistorySuccessDialog) {
              NotificationDialog(
                onConfirmClick = { isShowRemoveFromHistorySuccessDialog = false },
                icon = Icons.Default.Done,
                title = stringResource(R.string.you_have_removed_from_history_successfully),
                isEnableDismiss = false,
                confirm = stringResource(R.string.ok),
              )
            }
          }
        }

        if (isShowRemoveFromHistoryDialog && removeFromHistoryUiState.readingHistoryId != null) {
          NotificationDialog(
            onConfirmClick = {
              isShowRemoveFromHistoryDialog = false
              onRemoveFromHistory()
            },
            title = stringResource(R.string.are_you_sure_you_want_to_remove_it_from_your_history),
            onDismissClick = { isShowRemoveFromHistoryDialog = false },
            confirm = stringResource(R.string.remove),
          )
        }

        if (isShowNavigateDialog &&
          selectedMangaId != null &&
          selectedChapterId != null &&
          selectedLastReadPage != null
        ) {
          NotificationDialog(
            onConfirmClick = {
              isShowNavigateDialog = false
              onMangaDetailsClick(selectedMangaId!!)
            },
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
            onDismissOuterClick = { isShowNavigateDialog = false },
            confirm = stringResource(R.string.manga_details),
          )
        }
      }
    }
  }
}

private val previewHistoryItems = persistentListOf(
  ReadingHistoryModel(
    id = "rh-001",
    mangaId = "m-001",
    mangaTitle = "One Piece",
    mangaCoverUrl = "",
    chapterId = "c-001",
    chapterTitle = "Romance Dawn",
    chapterNumber = "1",
    chapterVolume = "1",
    lastReadPage = 12,
    pageCount = 46,
    lastReadAt = "2 hours ago",
  ),
  ReadingHistoryModel(
    id = "rh-002",
    mangaId = "m-002",
    mangaTitle = "Naruto",
    mangaCoverUrl = "",
    chapterId = "c-002",
    chapterTitle = "Uzumaki Naruto!!",
    chapterNumber = "1",
    chapterVolume = "1",
    lastReadPage = 5,
    pageCount = 53,
    lastReadAt = "Yesterday",
  ),
)

@Preview
@Composable
private fun HistoryContentFirstPageLoadingPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.FirstPageLoading,
      removeFromHistoryUiState = RemoveFromHistoryUiState(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun HistoryContentFirstPageErrorPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.FirstPageError(FeatureError.NetworkUnavailable),
      removeFromHistoryUiState = RemoveFromHistoryUiState(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun HistoryContentEmptyPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(),
        nextPageState = BaseNextPageState.NO_MORE_ITEMS
      ),
      removeFromHistoryUiState = RemoveFromHistoryUiState(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun HistoryContentWithItemsPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.Content(
        currentList = previewHistoryItems,
        nextPageState = BaseNextPageState.IDLE
      ),
      removeFromHistoryUiState = RemoveFromHistoryUiState(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun HistoryContentRemoveLoadingPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.Content(
        currentList = previewHistoryItems,
        nextPageState = BaseNextPageState.IDLE
      ),
      removeFromHistoryUiState = RemoveFromHistoryUiState(
        isLoading = true,
        readingHistoryId = "rh-001"
      ),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun HistoryContentRemoveSuccessPreview() {
  DexReaderTheme {
    HistoryContent(
      historyUiState = BasePaginationUiState.Content(
        currentList = previewHistoryItems,
        nextPageState = BaseNextPageState.IDLE
      ),
      removeFromHistoryUiState = RemoveFromHistoryUiState(isSuccess = true),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onUpdateRemoveReadingHistoryId = {},
      onRemoveFromHistory = {},
      onRetryRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      onRetryObserveHistoryFirstPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}