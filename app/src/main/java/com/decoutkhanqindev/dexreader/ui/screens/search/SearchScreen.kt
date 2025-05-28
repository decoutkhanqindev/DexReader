package com.decoutkhanqindev.dexreader.ui.screens.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.ui.components.content.bar.SearchMangaBar
import com.decoutkhanqindev.dexreader.ui.components.content.list.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.ui.components.content.text.AllItemLoadedText
import com.decoutkhanqindev.dexreader.ui.components.content.text.LoadMoreText
import com.decoutkhanqindev.dexreader.ui.components.states.ErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.IdleScreen
import com.decoutkhanqindev.dexreader.ui.components.states.ListLoadingScreen
import com.decoutkhanqindev.dexreader.ui.components.states.LoadPageErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.NextPageLoadingScreen
import com.decoutkhanqindev.dexreader.ui.components.states.NotFoundScreen

@Composable
fun SearchScreen(
  onSelectedManga: (String) -> Unit,
  onNavigateBack: () -> Unit,
  viewModel: SearchViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val mangaSuggestionsUiState by viewModel.mangaSuggestionsUiState.collectAsStateWithLifecycle()
  val mangaResultsUiState by viewModel.mangaResultsUiState.collectAsStateWithLifecycle()
  val mangaSuggestions by viewModel.mangaSuggestions.collectAsStateWithLifecycle()
  val query by viewModel.query.collectAsStateWithLifecycle()
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Scaffold(
    modifier = modifier,
    topBar = {
      SearchMangaBar(
        query = query,
        onQueryChange = {
          viewModel.updateQuery(it)
          isExpanded = true
        },
        onSearch = {
          viewModel.fetchMangaListFirstPage()
          isExpanded = false
        },
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxWidth()
      )
    },
    content = { innerPadding ->
      SearchContent(
        query = query,
        mangaSuggestionsUiState = mangaSuggestionsUiState,
        mangaResultsUiState = mangaResultsUiState,
        isExpanded = isExpanded,
        suggestionList = mangaSuggestions,
        onSelectedSuggestion = {
          viewModel.updateQuery(it)
          viewModel.fetchMangaListFirstPage()
          isExpanded = false
        },
        onFetchMangaListNextPage = { viewModel.fetchMangaListNextPage() },
        onRetryFetchMangaListNextPage = { viewModel.retryFetchMangaListNextPage() },
        onSelectedManga = onSelectedManga,
        onRetry = { viewModel.retry() },
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      )
    }
  )
}

@Composable
private fun SearchContent(
  query: String,
  mangaSuggestionsUiState: SearchMangaSuggestionsUiState,
  mangaResultsUiState: SearchMangaResultsUiState,
  isExpanded: Boolean,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onSelectedManga: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (query.isEmpty()) {
    IdleScreen(
      message = stringResource(R.string.search_your_manga_here),
      modifier = modifier
    )
    return
  }

  if (isExpanded) {
    SearchMangaSuggestions(
      query = query,
      mangaSuggestionsUiState = mangaSuggestionsUiState,
      suggestionList = suggestionList,
      onSelectedSuggestion = onSelectedSuggestion,
      onRetry = onRetry,
      modifier = modifier
    )
  } else {
    SearchMangaResults(
      query = query,
      mangaResultsUiState = mangaResultsUiState,
      onSelectedManga = onSelectedManga,
      onFetchMangaListNextPage = onFetchMangaListNextPage,
      onRetryFetchMangaListNextPage = onRetryFetchMangaListNextPage,
      onRetry = onRetry,
      modifier = modifier
    )
  }
}

@Composable
fun SearchMangaSuggestions(
  query: String,
  mangaSuggestionsUiState: SearchMangaSuggestionsUiState,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (mangaSuggestionsUiState) {
    SearchMangaSuggestionsUiState.Loading -> ListLoadingScreen(modifier = modifier)

    SearchMangaSuggestionsUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    SearchMangaSuggestionsUiState.Success -> {
      if (suggestionList.isEmpty()) {
        NotFoundScreen(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      } else {
        MangaSuggestionList(
          suggestionList = suggestionList,
          onSelectedSuggestion = onSelectedSuggestion,
          modifier = modifier
        )
      }
    }
  }
}

@Composable
fun SearchMangaResults(
  query: String,
  mangaResultsUiState: SearchMangaResultsUiState,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (mangaResultsUiState) {
    SearchMangaResultsUiState.FirstPageLoading -> ListLoadingScreen(modifier = modifier)

    SearchMangaResultsUiState.FirstPageError -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is SearchMangaResultsUiState.Content -> {
      val mangaList = mangaResultsUiState.mangaList
      val nextPageState = mangaResultsUiState.nextPageState

      if (mangaList.isEmpty()) {
        NotFoundScreen(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      } else {
        VerticalGridMangaList(
          mangaList = mangaList,
          onSelectedManga = { onSelectedManga(it.id) },
          loadMoreContent = {
            when (nextPageState) {
              SearchMangaResultsNextPageState.LOADING -> NextPageLoadingScreen(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 12.dp)
              )

              SearchMangaResultsNextPageState.ERROR -> LoadPageErrorScreen(
                message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                onRetry = onRetryFetchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              )

              SearchMangaResultsNextPageState.IDLE -> LoadMoreText(
                onLoadMore = onFetchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )

              SearchMangaResultsNextPageState.NO_MORE_ITEMS -> AllItemLoadedText(
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

@Composable
private fun MangaSuggestionList(
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    items(suggestionList) { suggestion ->
      MangaSuggestionItem(
        suggestion = suggestion,
        onSelectedSuggestion = onSelectedSuggestion,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun MangaSuggestionItem(
  suggestion: String,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  DropdownMenuItem(
    text = {
      Text(
        text = suggestion,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Light,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    onClick = { onSelectedSuggestion(suggestion) },
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.search)
      )
    },
    modifier = modifier
  )
}