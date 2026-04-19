package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaChaptersSection(
  mangaChaptersUiState: BasePaginationUiState<ChapterModel>,
  latestChapter: String,
  chapterLanguage: MangaLanguageValue,
  chapterLanguageList: ImmutableList<MangaLanguageValue>,
  readingHistoryList: ImmutableList<ReadingHistoryModel> = persistentListOf(),
  modifier: Modifier = Modifier,
  onLanguageItemClick: (MangaLanguageValue) -> Unit,
  onChapterItemClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetry: () -> Unit,
) {
  Column(modifier = modifier) {
    MangaChaptersHeader(
      selectedLanguage = chapterLanguage,
      languageList = chapterLanguageList,
      modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
        .padding(horizontal = 4.dp),
    ) { onLanguageItemClick(it) }

    when (mangaChaptersUiState) {
      BasePaginationUiState.FirstPageLoading -> ListLoadingIndicator(modifier = Modifier.fillMaxSize())

      is BasePaginationUiState.FirstPageError -> LoadPageErrorMessage(
        message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 4.dp),
      ) { onRetry() }

      is BasePaginationUiState.Content -> {
        val chapterList = mangaChaptersUiState.currentList
        val chapterListNextPageState = mangaChaptersUiState.nextPageState

        MangaChapterList(
          lastItem = latestChapter,
          chapterList = chapterList,
          chapterListNextPageState = chapterListNextPageState,
          readingHistoryList = readingHistoryList,
          modifier = Modifier.fillMaxWidth(),
          onChapterClick = onChapterItemClick,
          onFetchChapterListNextPage = onFetchChapterListNextPage,
          onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
        )
      }
    }
  }
}

@Preview
@Composable
private fun MangaChaptersSectionFirstPageLoadingPreview() {
  MangaChaptersSection(
    mangaChaptersUiState = BasePaginationUiState.FirstPageLoading,
    latestChapter = "1110",
    chapterLanguage = MangaLanguageValue.ENGLISH,
    chapterLanguageList = persistentListOf(MangaLanguageValue.ENGLISH, MangaLanguageValue.JAPANESE),
    modifier = Modifier.fillMaxWidth(),
    onLanguageItemClick = {},
    onChapterItemClick = { _, _, _ -> },
    onFetchChapterListNextPage = {},
    onRetryFetchChapterListNextPage = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun MangaChaptersSectionFirstPageErrorPreview() {
  MangaChaptersSection(
    mangaChaptersUiState = BasePaginationUiState.FirstPageError(FeatureError.NetworkUnavailable),
    latestChapter = "1110",
    chapterLanguage = MangaLanguageValue.ENGLISH,
    chapterLanguageList = persistentListOf(MangaLanguageValue.ENGLISH),
    modifier = Modifier.fillMaxWidth(),
    onLanguageItemClick = {},
    onChapterItemClick = { _, _, _ -> },
    onFetchChapterListNextPage = {},
    onRetryFetchChapterListNextPage = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun MangaChaptersSectionContentPreview() {
  MangaChaptersSection(
    mangaChaptersUiState = BasePaginationUiState.Content(
      currentList = persistentListOf(
        ChapterModel(id = "c-001", mangaId = "m-001", title = "Romance Dawn", number = "1", volume = "1", publishedAt = "2024-01-01"),
        ChapterModel(id = "c-002", mangaId = "m-001", title = "They Call Him 'Straw Hat Luffy'", number = "2", volume = "1", publishedAt = "2024-01-08"),
      )
    ),
    latestChapter = "1110",
    chapterLanguage = MangaLanguageValue.ENGLISH,
    chapterLanguageList = persistentListOf(MangaLanguageValue.ENGLISH, MangaLanguageValue.JAPANESE),
    modifier = Modifier.fillMaxWidth(),
    onLanguageItemClick = {},
    onChapterItemClick = { _, _, _ -> },
    onFetchChapterListNextPage = {},
    onRetryFetchChapterListNextPage = {},
    onRetry = {}
  )
}
