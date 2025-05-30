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
import com.decoutkhanqindev.dexreader.presentation.ui.components.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.components.common.texts.AllItemLoadedText
import com.decoutkhanqindev.dexreader.presentation.ui.components.common.texts.LoadMoreText
import com.decoutkhanqindev.dexreader.presentation.ui.components.common.texts.NextPageErrorText
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
        MangaChaptersNextPageState.LOADING ->
          NextPageLoadingIndicator(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )

        MangaChaptersNextPageState.ERROR ->
          NextPageErrorText(
            message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
            onRetryFetchNextPage = onRetryFetchChapterListNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
          )

        MangaChaptersNextPageState.IDLE ->
          LoadMoreText(
            onLoadMore = onFetchChapterListNextPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp)
              .padding(bottom = 12.dp)
          )

        MangaChaptersNextPageState.NO_MORE_ITEMS ->
          AllItemLoadedText(
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
