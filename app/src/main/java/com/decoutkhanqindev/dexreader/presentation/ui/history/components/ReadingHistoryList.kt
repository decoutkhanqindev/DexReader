package com.decoutkhanqindev.dexreader.presentation.ui.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.ui.history.HistoryNextPageState
import kotlinx.coroutines.launch

@Composable
fun ReadingHistoryList(
  readingHistoryList: List<ReadingHistory>,
  historyNextPageState: HistoryNextPageState,
  onSelectedReadingHistory: (
    mangaId: String,
    chapterId: String,
    lastReadPage: Int
  ) -> Unit,
  onRemoveFromHistory: (String) -> Unit,
  onObserveHistoryNextPage: () -> Unit,
  onRetryObserveHistoryNextPage: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible = readingHistoryList.size > 7 &&
      lazyListState.firstVisibleItemIndex > 0

  Box(modifier = modifier) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier.fillMaxSize(),
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
            .height(160.dp)
        )
        HorizontalDivider(
          thickness = 2.dp,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        when (historyNextPageState) {
          HistoryNextPageState.LOADING -> NextPageLoadingIndicator(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )

          HistoryNextPageState.ERROR -> LoadPageErrorMessage(
            message = stringResource(R.string.can_t_load_next_reading_history_page_please_try_again),
            onRetry = onRetryObserveHistoryNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
          )

          HistoryNextPageState.IDLE -> LoadMoreMessage(
            onLoadMore = onObserveHistoryNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(bottom = 12.dp, top = 8.dp)
          )

          HistoryNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
            title = "All reading histories loaded.",
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(bottom = 12.dp, top = 8.dp)
          )
        }
      }
    }

    AnimatedVisibility(
      visible = isMoveToTopButtonVisible,
      enter = scaleIn(),
      exit = scaleOut(),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      MoveToTopButton(
        onClick = {
          coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
          }
        },
        modifier = Modifier.size(56.dp)
      )
    }
  }
}