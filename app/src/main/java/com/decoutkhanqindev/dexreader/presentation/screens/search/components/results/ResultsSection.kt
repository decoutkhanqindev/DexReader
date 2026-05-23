package com.decoutkhanqindev.dexreader.presentation.screens.search.components.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun ResultsSection(
  query: String,
  resultsUiState: BasePaginationUiState<MangaModel>,
  modifier: Modifier = Modifier,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
) {
  val gridState = rememberLazyGridState()
  val coroutineScope = rememberCoroutineScope()
  val mangaListSize = (resultsUiState as? BasePaginationUiState.Content)?.currentList?.size ?: 0
  var isShowErrorDialog by remember(resultsUiState) { mutableStateOf(resultsUiState is BasePaginationUiState.FirstPageError) }

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    when (resultsUiState) {
      BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is BasePaginationUiState.FirstPageError -> {
        if (isShowErrorDialog) {
          AlertDialog(
            title = stringResource(resultsUiState.error.messageRes),
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      is BasePaginationUiState.Content -> {
        val mangaList = resultsUiState.currentList
        val nextPageState = resultsUiState.nextPageState

        if (mangaList.isEmpty()) {
          ResultsNotFoundMessage(
            message = stringResource(R.string.sorry_no_manga_found_with_title, query),
            modifier = Modifier.fillMaxWidth()
          )
        } else {
          VerticalGridMangaList(
            lazyGridState = gridState,
            items = mangaList,
            modifier = Modifier.fillMaxSize(),
            onItemClick = onSelectedManga,
          ) {
            when (nextPageState) {
              BaseNextPageState.LOADING -> ListLoadingIndicator(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 12.dp)
              )

              BaseNextPageState.ERROR -> LoadPageErrorMessage(
                message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp),
              ) { onRetryFetchMangaListNextPage() }

              BaseNextPageState.IDLE -> LoadMoreMessage(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp),
              ) { onFetchMangaListNextPage() }

              BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
                title = stringResource(R.string.all_mangas_loaded),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )
            }
          }
        }
      }
    }

    MoveToTopButton(
      itemsSize = mangaListSize,
      gridState = gridState,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      coroutineScope.launch {
        gridState.animateScrollToItem(0)
      }
    }
  }
}

private val previewManga = MangaModel(
  id = "manga-001",
  title = "One Piece",
  coverUrl = "",
  description = "A boy who ate a Devil Fruit gains the power to stretch like rubber.",
  author = "Eiichiro Oda",
  artist = "Eiichiro Oda",
  categories = persistentListOf(),
  status = MangaStatusValue.ON_GOING,
  contentRating = MangaContentRatingValue.SAFE,
  year = "1997",
  availableLanguages = persistentListOf(),
  latestChapter = "1100",
  updatedAt = "2 days ago",
)

@Preview(showBackground = true)
@Composable
private fun ResultsSectionLoadingPreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.FirstPageLoading,
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionErrorPreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.FirstPageError(),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionEmptyPreview() {
  DexReaderTheme {
    ResultsSection(
      query = "Dragon Ball",
      resultsUiState = BasePaginationUiState.Content<MangaModel>(),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionContentIdlePreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(previewManga),
        nextPageState = BaseNextPageState.IDLE,
      ),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionContentLoadingNextPagePreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(previewManga),
        nextPageState = BaseNextPageState.LOADING,
      ),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionContentNextPageErrorPreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(previewManga),
        nextPageState = BaseNextPageState.ERROR,
      ),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ResultsSectionContentNoMoreItemsPreview() {
  DexReaderTheme {
    ResultsSection(
      query = "One Piece",
      resultsUiState = BasePaginationUiState.Content(
        currentList = persistentListOf(previewManga),
        nextPageState = BaseNextPageState.NO_MORE_ITEMS,
      ),
      modifier = Modifier.fillMaxSize(),
      onSelectedManga = {},
      onFetchMangaListNextPage = {},
      onRetryFetchMangaListNextPage = {},
      onRetry = {},
    )
  }
}