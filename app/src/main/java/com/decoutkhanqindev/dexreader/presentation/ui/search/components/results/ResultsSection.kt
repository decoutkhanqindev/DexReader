package com.decoutkhanqindev.dexreader.presentation.ui.search.components.results

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.lists.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.ui.search.ResultsNextPageState
import com.decoutkhanqindev.dexreader.presentation.ui.search.ResultsUiState
import com.decoutkhanqindev.dexreader.presentation.ui.search.components.SearchNotFoundMessage

@Composable
fun ResultsSection(
  query: String,
  resultsUiState: ResultsUiState,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (resultsUiState) {
    ResultsUiState.FirstPageLoading -> ListLoadingIndicator(modifier = modifier)

    ResultsUiState.FirstPageError -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is ResultsUiState.Content -> {
      val mangaList = resultsUiState.results
      val nextPageState = resultsUiState.nextPageState

      if (mangaList.isEmpty()) {
        SearchNotFoundMessage(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      } else {
        VerticalGridMangaList(
          mangaList = mangaList,
          onSelectedManga = { onSelectedManga(it.id) },
          loadMoreContent = {
            when (nextPageState) {
              ResultsNextPageState.LOADING -> NextPageLoadingIndicator(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 12.dp)
              )

              ResultsNextPageState.ERROR -> LoadPageErrorMessage(
                message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                onRetry = onRetryFetchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              )

              ResultsNextPageState.IDLE -> LoadMoreMessage(
                onLoadMore = onFetchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )

              ResultsNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
                title = stringResource(R.string.all_mangas_loaded),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )
            }
          },
          modifier = modifier
        )
      }
    }
  }
}