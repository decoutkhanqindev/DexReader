package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.ui.manga_details.MangaChaptersNextPageState

@Composable
fun MangaChapterList(
  chapterList: List<Chapter>,
  onSelectedChapter: (String) -> Unit,
  chapterListNextPageState: MangaChaptersNextPageState,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    if (chapterList.isEmpty()) {
      NoChaptersAvailableMessage(modifier = Modifier.fillMaxWidth())
    } else {
      chapterList.forEach { chapter ->
        MangaChapterItem(
          chapter = chapter,
          onSelectedChapter = onSelectedChapter,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 4.dp)
        )
      }

      when (chapterListNextPageState) {
        MangaChaptersNextPageState.LOADING -> NextPageLoadingIndicator(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        )

        MangaChaptersNextPageState.ERROR -> LoadPageErrorMessage(
          message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
          onRetry = onRetryFetchChapterListNextPage,
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
        )

        MangaChaptersNextPageState.IDLE -> LoadMoreMessage(
          onLoadMore = onFetchChapterListNextPage,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp)
        )

        MangaChaptersNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
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
