package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage

@Composable
fun MangaChapterList(
  latestChapter: String,
  chapterList: List<Chapter>,
  readingHistoryList: List<ReadingHistory> = emptyList(),
  onSelectedChapter: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  chapterListNextPageState: BaseNextPageState,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    if (chapterList.isEmpty()) {
      Text(
        text = stringResource(R.string.no_chapters_available),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    } else {
      chapterList.forEach { chapter ->
        val readingHistory = readingHistoryList.find { it.chapterId == chapter.id }

        MangaChapterItem(
          latestChapter = latestChapter,
          chapter = chapter,
          readingHistory = readingHistory,
          onSelectedChapter = onSelectedChapter,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 4.dp)
        )
      }

      when (chapterListNextPageState) {
        BaseNextPageState.LOADING -> NextPageLoadingIndicator(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        )

        BaseNextPageState.ERROR -> LoadPageErrorMessage(
          message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
          onRetry = onRetryFetchChapterListNextPage,
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
        )

        BaseNextPageState.IDLE -> LoadMoreMessage(
          onLoadMore = onFetchChapterListNextPage,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp)
        )

        BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
          title = stringResource(R.string.all_chapters_loaded),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp)
        )
      }
    }
  }
}