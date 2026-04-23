package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions.ActionsSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters.MangaChaptersSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info.MangaInfoSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info.previewManga
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary.MangaSummarySection
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MangaDetailsContent(
  mangaDetailsUiState: MangaDetailsUiState,
  mangaChaptersUiState: BasePaginationUiState<ChapterModel>,
  isFavorite: Boolean,
  chapterLanguage: MangaLanguageValue,
  availableLanguageList: ImmutableList<MangaLanguageValue>,
  readingHistoryList: ImmutableList<ReadingHistoryModel> = persistentListOf(),
  startedChapterId: String? = null,
  continueChapter: ReadingHistoryModel? = null,
  modifier: Modifier = Modifier,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFavoriteClick: () -> Unit,
  onLanguageItemClick: (MangaLanguageValue) -> Unit,
  onCategoryItemClick: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
  onChapterItemClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListNextPage: () -> Unit,
  onRetryFetchChapterListFirstPage: () -> Unit,
  onRetry: () -> Unit,
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  var mangaId by rememberSaveable { mutableStateOf("") }
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  Box(modifier = modifier) {
    when (mangaDetailsUiState) {
      MangaDetailsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is MangaDetailsUiState.Error -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            title = stringResource(mangaDetailsUiState.error.messageRes),
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      is MangaDetailsUiState.Success -> {
        val manga = mangaDetailsUiState.manga
        val mangaCoverUrl = manga.coverUrl
        val latestChapter = manga.latestChapter

        mangaId = manga.id

        Box(modifier = Modifier.fillMaxSize()) {
          MangaDetailsBackground(
            imageUrl = mangaCoverUrl,
            modifier = Modifier.fillMaxSize()
          )

          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .blurBackground(
                topAlpha = 0.7f,
                bottomAlpha = 1f,
              ),
            state = lazyListState,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            item {
              MangaInfoSection(
                manga = manga,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(4.dp)
                  .padding(top = 8.dp, bottom = 16.dp)
              )
            }

            item {
              MangaSummarySection(
                manga = manga,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 16.dp),
              ) { categoryId, categoryTitle ->
                onCategoryItemClick(categoryId, categoryTitle)
              }
            }

            item {
              MangaChaptersSection(
                mangaChaptersUiState = mangaChaptersUiState,
                latestChapter = latestChapter,
                chapterLanguage = chapterLanguage,
                chapterLanguageList = availableLanguageList,
                readingHistoryList = readingHistoryList,
                modifier = Modifier.fillMaxWidth(),
                onLanguageItemClick = onLanguageItemClick,
                onChapterItemClick = onChapterItemClick,
                onFetchChapterListNextPage = onFetchChapterListNextPage,
                onRetryFetchChapterListNextPage = onRetryFetchChapterListNextPage,
                onRetry = onRetryFetchChapterListFirstPage,
              )
            }

            item {
              Spacer(modifier = Modifier.height(70.dp))
            }
          }

          ActionsSection(
            itemsSize = (mangaChaptersUiState as? BasePaginationUiState.Content<ChapterModel>)?.currentList?.size ?: 0,
            listState = lazyListState,
            isFavorite = isFavorite,
            startedChapterId = startedChapterId,
            mangaId = mangaId,
            continueChapter = continueChapter,
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter),
            onMoveToTopClick = {
              coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
              }
            },
            onReadingClick = onReadingClick,
            onFavoriteClick = onFavoriteClick,
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun MangaDetailsContentLoadingPreview() {
  DexReaderTheme {
    MangaDetailsContent(
      mangaDetailsUiState = MangaDetailsUiState.Loading,
      mangaChaptersUiState = BasePaginationUiState.FirstPageLoading,
      isFavorite = false,
      chapterLanguage = MangaLanguageValue.ENGLISH,
      availableLanguageList = persistentListOf(MangaLanguageValue.ENGLISH),
      modifier = Modifier.fillMaxSize(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {},
      onLanguageItemClick = {},
      onCategoryItemClick = { _, _ -> },
      onChapterItemClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {},
      onRetryFetchChapterListFirstPage = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun MangaDetailsContentErrorPreview() {
  DexReaderTheme {
    MangaDetailsContent(
      mangaDetailsUiState = MangaDetailsUiState.Error(FeatureError.NetworkUnavailable),
      mangaChaptersUiState = BasePaginationUiState.FirstPageLoading,
      isFavorite = false,
      chapterLanguage = MangaLanguageValue.ENGLISH,
      availableLanguageList = persistentListOf(MangaLanguageValue.ENGLISH),
      modifier = Modifier.fillMaxSize(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {},
      onLanguageItemClick = {},
      onCategoryItemClick = { _, _ -> },
      onChapterItemClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {},
      onRetryFetchChapterListFirstPage = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun MangaDetailsContentSuccessPreview() {
  DexReaderTheme {
    MangaDetailsContent(
      mangaDetailsUiState = MangaDetailsUiState.Success(previewManga),
      mangaChaptersUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(
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
        ),
        nextPageState = BaseNextPageState.IDLE
      ),
      isFavorite = false,
      chapterLanguage = MangaLanguageValue.ENGLISH,
      availableLanguageList = persistentListOf(
        MangaLanguageValue.ENGLISH,
        MangaLanguageValue.JAPANESE
      ),
      startedChapterId = "c-001",
      modifier = Modifier.fillMaxSize(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {},
      onLanguageItemClick = {},
      onCategoryItemClick = { _, _ -> },
      onChapterItemClick = { _, _, _ -> },
      onFetchChapterListNextPage = {},
      onRetryFetchChapterListNextPage = {},
      onRetryFetchChapterListFirstPage = {},
      onRetry = {}
    )
  }
}
