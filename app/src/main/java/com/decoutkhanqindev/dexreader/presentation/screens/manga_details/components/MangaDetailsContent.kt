package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.MangaDetailsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions.ReadingAndFavoriteSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters.MangaChapterItem
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters.MangaChaptersHeader
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info.MangaInfoSection
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info.previewManga
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary.MangaSummarySection
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

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
    var isShowErrorDialog by remember(mangaDetailsUiState) { mutableStateOf(mangaDetailsUiState is MangaDetailsUiState.Error) }

    val topCardShape = remember { RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp) }
    val bottomCardShape = remember { RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp) }

    Box(modifier = modifier) {
        when (mangaDetailsUiState) {
            MangaDetailsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

            is MangaDetailsUiState.Error -> {
                if (isShowErrorDialog) {
                    AlertDialog(
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
                val historyByChapterId = remember(readingHistoryList) {
                    readingHistoryList.associateBy { it.chapterId }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    MangaDetailsBackground(
                        url = mangaCoverUrl,
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            )
                        }

                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = topCardShape,
                                tonalElevation = 4.dp,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    MangaInfoSection(
                                        manga = manga,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    MangaSummarySection(
                                        manga = manga,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        onCategoryItemClick = onCategoryItemClick
                                    )

                                    MangaChaptersHeader(
                                        selectedLanguage = chapterLanguage,
                                        languageList = availableLanguageList,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 24.dp, bottom = 12.dp),
                                        onLanguageItemClick = onLanguageItemClick
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }

                        when (mangaChaptersUiState) {
                            BasePaginationUiState.FirstPageLoading -> item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 4.dp
                                ) {
                                    ListLoadingIndicator(modifier = Modifier.padding(24.dp))
                                }
                            }

                            is BasePaginationUiState.FirstPageError -> item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 4.dp
                                ) {
                                    LoadPageErrorMessage(
                                        message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        onRetryClick = onRetryFetchChapterListFirstPage
                                    )
                                }
                            }

                            is BasePaginationUiState.Content -> {
                                val chapterList = mangaChaptersUiState.currentList
                                val nextPageState = mangaChaptersUiState.nextPageState

                                if (chapterList.isEmpty()) {
                                    item {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = bottomCardShape,
                                            color = MaterialTheme.colorScheme.surface,
                                            tonalElevation = 4.dp
                                        ) {
                                            Text(
                                                text = stringResource(R.string.no_chapters_available),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(24.dp),
                                                fontStyle = FontStyle.Italic,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        }
                                    }
                                } else {
                                    items(chapterList, key = { it.id }) { chapter ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth().animateItem(),
                                            color = MaterialTheme.colorScheme.surface,
                                            tonalElevation = 4.dp
                                        ) {
                                            MangaChapterItem(
                                                lastChapter = latestChapter,
                                                chapter = chapter,
                                                readingHistory = historyByChapterId[chapter.id],
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                onChapterClick = onChapterItemClick,
                                            )
                                        }
                                    }

                                    item {
                                        val isLast = nextPageState == BaseNextPageState.NO_MORE_ITEMS
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = if (isLast) bottomCardShape else RoundedCornerShape(0.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            tonalElevation = 4.dp
                                        ) {
                                            Surface(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                            ) {
                                                Box(modifier = Modifier.fillMaxWidth()) {
                                                    when (nextPageState) {
                                                        BaseNextPageState.LOADING -> ListLoadingIndicator(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 12.dp)
                                                        )

                                                        BaseNextPageState.ERROR -> LoadPageErrorMessage(
                                                            message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 8.dp),
                                                            onRetryClick = onRetryFetchChapterListNextPage
                                                        )

                                                        BaseNextPageState.IDLE -> LoadMoreMessage(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 12.dp),
                                                            onClick = onFetchChapterListNextPage
                                                        )

                                                        BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
                                                            title = stringResource(R.string.all_chapters_loaded),
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 12.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }

                    ReadingAndFavoriteSection(
                        itemsSize = (mangaChaptersUiState as? BasePaginationUiState.Content<ChapterModel>)?.currentList?.size
                            ?: 0,
                        listState = lazyListState,
                        isFavorite = isFavorite,
                        startedChapterId = startedChapterId,
                        mangaId = manga.id,
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
