package com.decoutkhanqindev.dexreader.presentation.screens.history.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun ReadingHistoryList(
  readingHistoryList: ImmutableList<ReadingHistoryModel>,
  historyNextPageState: BaseNextPageState,
  onSelectedReadingHistory: (
    mangaId: String,
    chapterId: String,
    lastReadPage: Int,
  ) -> Unit,
  onRemoveFromHistory: (String) -> Unit,
  onObserveHistoryNextPage: () -> Unit,
  onRetryObserveHistoryNextPage: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  Box(modifier = modifier) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(2.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      itemsIndexed(
        items = readingHistoryList,
        key = { index, readingHistory -> "${readingHistory.id}_${index}" }
      ) { index, readingHistory ->
        ReadingHistoryItem(
          readingHistory = readingHistory,
          onSelectedReadingHistory = onSelectedReadingHistory,
          onRemoveFromHistory = onRemoveFromHistory,
          modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .height(160.dp)
        )
      }

      item {
        when (historyNextPageState) {
          BaseNextPageState.LOADING -> NextPageLoadingIndicator(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )

          BaseNextPageState.ERROR -> LoadPageErrorMessage(
            message = stringResource(R.string.can_t_load_next_reading_history_page_please_try_again),
            onRetryClick = onRetryObserveHistoryNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
          )

          BaseNextPageState.IDLE -> LoadMoreMessage(
            onClick = onObserveHistoryNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(bottom = 12.dp, top = 8.dp)
          )

          BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
            title = "All reading histories loaded.",
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(bottom = 12.dp, top = 8.dp)
          )
        }
      }
    }

    MoveToTopButton(
      itemsSize = readingHistoryList.size,
      firstVisibleItemIndex = lazyListState.firstVisibleItemIndex,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      coroutineScope.launch {
        lazyListState.animateScrollToItem(0)
      }
    }
  }
}

private val previewHistoryList = persistentListOf(
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
private fun ReadingHistoryListIdlePreview() {
  DexReaderTheme {
    ReadingHistoryList(
      readingHistoryList = previewHistoryList,
      historyNextPageState = BaseNextPageState.IDLE,
      onSelectedReadingHistory = { _, _, _ -> },
      onRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun ReadingHistoryListNextPageLoadingPreview() {
  DexReaderTheme {
    ReadingHistoryList(
      readingHistoryList = previewHistoryList,
      historyNextPageState = BaseNextPageState.LOADING,
      onSelectedReadingHistory = { _, _, _ -> },
      onRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun ReadingHistoryListNextPageErrorPreview() {
  DexReaderTheme {
    ReadingHistoryList(
      readingHistoryList = previewHistoryList,
      historyNextPageState = BaseNextPageState.ERROR,
      onSelectedReadingHistory = { _, _, _ -> },
      onRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun ReadingHistoryListNoMoreItemsPreview() {
  DexReaderTheme {
    ReadingHistoryList(
      readingHistoryList = previewHistoryList,
      historyNextPageState = BaseNextPageState.NO_MORE_ITEMS,
      onSelectedReadingHistory = { _, _, _ -> },
      onRemoveFromHistory = {},
      onObserveHistoryNextPage = {},
      onRetryObserveHistoryNextPage = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}