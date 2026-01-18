package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import kotlinx.coroutines.launch

@Composable
fun ReadingHistoryList(
  readingHistoryList: List<ReadingHistory>,
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
  val isMoveToTopButtonVisible = readingHistoryList.size > 7 &&
      lazyListState.firstVisibleItemIndex > 0

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
            onRetry = onRetryObserveHistoryNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
          )

          BaseNextPageState.IDLE -> LoadMoreMessage(
            onLoadMore = onObserveHistoryNextPage,
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