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
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaChaptersSection(
  mangaChaptersUiState: BasePaginationUiState<ChapterModel>,
  readingHistoryList: ImmutableList<ReadingHistoryModel> = persistentListOf(),
  latestChapter: String,
  chapterLanguage: MangaLanguageValue,
  chapterLanguageList: ImmutableList<MangaLanguageValue>,
  onSelectedLanguage: (MangaLanguageValue) -> Unit,
  onSelectedChapter: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    MangaChaptersHeader(
      selectedLanguage = chapterLanguage,
      languageList = chapterLanguageList,
      onSelectedLanguage = onSelectedLanguage,
      modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
        .padding(horizontal = 4.dp)
    )

    when (mangaChaptersUiState) {
      BasePaginationUiState.FirstPageLoading -> ListLoadingIndicator(modifier = Modifier.fillMaxSize())

      is BasePaginationUiState.FirstPageError -> LoadPageErrorMessage(
        message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
        onRetry = onRetry,
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 4.dp)
      )

      is BasePaginationUiState.Content -> {
        val chapterList = mangaChaptersUiState.currentList
        val chapterListNextPageState = mangaChaptersUiState.nextPageState

        MangaChapterList(
          latestChapter = latestChapter,
          chapterList = chapterList,
          readingHistoryList = readingHistoryList,
          onSelectedChapter = onSelectedChapter,
          chapterListNextPageState = chapterListNextPageState,
          onFetchChapterListNextPage = onFetchChapterListNextPage,
          onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}
