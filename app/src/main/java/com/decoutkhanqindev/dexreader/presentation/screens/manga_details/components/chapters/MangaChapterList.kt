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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaChapterList(
  lastItem: String,
  chapterList: ImmutableList<ChapterModel>,
  chapterListNextPageState: BaseNextPageState,
  readingHistoryList: ImmutableList<ReadingHistoryModel> = persistentListOf(),
  modifier: Modifier = Modifier,
  onChapterClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
) {
  Column(modifier = modifier) {
    if (chapterList.isEmpty()) {
      Text(
        text = stringResource(R.string.no_chapters_available),
        modifier = Modifier.fillMaxWidth(),
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
      )
    } else {
      chapterList.forEach { chapter ->
        val readingHistory = readingHistoryList.find { it.chapterId == chapter.id }

        MangaChapterItem(
          lastChapter = lastItem,
          chapter = chapter,
          readingHistory = readingHistory,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 4.dp),
        ) { chapterId, lastReadPage, mangaId ->
          onChapterClick(chapterId, lastReadPage, mangaId)
        }
      }

      when (chapterListNextPageState) {
        BaseNextPageState.LOADING -> ListLoadingIndicator(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        )

        BaseNextPageState.ERROR -> LoadPageErrorMessage(
          message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        ) { onRetryFetchChapterListNextPage() }

        BaseNextPageState.IDLE -> LoadMoreMessage(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp),
        ) { onFetchChapterListNextPage() }

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

private val previewChapterList = persistentListOf(
  ChapterModel(
    id = "c-001",
    mangaId = "m-001",
    title = "Romance Dawn",
    number = "1",
    volume = "1",
    publishedAt = "2024-01-01"
  ),
  ChapterModel(
    id = "c-002",
    mangaId = "m-001",
    title = "They Call Him 'Straw Hat Luffy'",
    number = "2",
    volume = "1",
    publishedAt = "2024-01-08"
  ),
  ChapterModel(
    id = "c-003",
    mangaId = "m-001",
    title = "Enter Zoro the Pirate Hunter",
    number = "3",
    volume = "1",
    publishedAt = "2024-01-15"
  ),
)

@Preview
@Composable
private fun MangaChapterListEmptyPreview() {
  DexReaderTheme {
    MangaChapterList(
      lastItem = "1110",
      chapterList = persistentListOf(),
      chapterListNextPageState = BaseNextPageState.NO_MORE_ITEMS,
      modifier = Modifier.fillMaxWidth(),
      onChapterClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {}
    )
  }
}

@Preview
@Composable
private fun MangaChapterListIdlePreview() {
  DexReaderTheme {
    MangaChapterList(
      lastItem = "1110",
      chapterList = previewChapterList,
      chapterListNextPageState = BaseNextPageState.IDLE,
      modifier = Modifier.fillMaxWidth(),
      onChapterClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {}
    )
  }
}

@Preview
@Composable
private fun MangaChapterListNoMoreItemsPreview() {
  DexReaderTheme {
    MangaChapterList(
      lastItem = "3",
      chapterList = previewChapterList,
      chapterListNextPageState = BaseNextPageState.NO_MORE_ITEMS,
      modifier = Modifier.fillMaxWidth(),
      onChapterClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {}
    )
  }
}
