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
import com.decoutkhanqindev.dexreader.ui.components.bar.SearchMangaBar
import com.decoutkhanqindev.dexreader.ui.components.content.AllItemLoadedText
import com.decoutkhanqindev.dexreader.ui.components.content.LoadMoreText
import com.decoutkhanqindev.dexreader.ui.components.content.VerticalGridMangaList
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
  val suggestionsUiState by viewModel.suggestionsUiState.collectAsStateWithLifecycle()
  val resultsUiState by viewModel.resultsUiState.collectAsStateWithLifecycle()
  val suggestionList by viewModel.suggestionList.collectAsStateWithLifecycle()
  val query by viewModel.query.collectAsStateWithLifecycle()
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Scaffold(
    modifier = modifier,
    topBar = {
      SearchMangaBar(
        query = query,
        onQueryChange = {
          viewModel.onQueryChange(it)
          isExpanded = true
        },
        onSearch = {
          viewModel.loadSearchMangaListFirstPage()
          isExpanded = false
        },
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxWidth()
      )
    },
    content = { innerPadding ->
      SearchContent(
        query = query,
        suggestionsUiState = suggestionsUiState,
        resultsUiState = resultsUiState,
        isExpanded = isExpanded,
        suggestionList = suggestionList,
        onSelectedSuggestion = {
          viewModel.onQueryChange(it)
          viewModel.loadSearchMangaListFirstPage()
          isExpanded = false
        },
        onLoadSearchMangaListNextPage = { viewModel.loadSearchMangaListNextPage() },
        onRetryLoadSearchMangaListNextPage = { viewModel.retryLoadSearchMangaListNextPage() },
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
  suggestionsUiState: SearchSuggestionsUiState,
  resultsUiState: SearchResultsUiState,
  isExpanded: Boolean,
  suggestionList: List<String>,
  onLoadSearchMangaListNextPage: () -> Unit,
  onRetryLoadSearchMangaListNextPage: () -> Unit,
  onSelectedSuggestion: (String) -> Unit,
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
    MangaListSuggestions(
      query = query,
      suggestionsUiState = suggestionsUiState,
      suggestionList = suggestionList,
      onSelectedSuggestion = onSelectedSuggestion,
      onRetry = onRetry,
      modifier = modifier
    )
  } else {
    MangaListResults(
      query = query,
      resultsUiState = resultsUiState,
      onSelectedManga = onSelectedManga,
      onLoadSearchMangaListNextPage = onLoadSearchMangaListNextPage,
      onRetryLoadSearchMangaListNextPage = onRetryLoadSearchMangaListNextPage,
      onRetry = onRetry,
      modifier = modifier
    )
  }
}

@Composable
fun MangaListSuggestions(
  query: String,
  suggestionsUiState: SearchSuggestionsUiState,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (suggestionsUiState) {
    SearchSuggestionsUiState.Loading -> ListLoadingScreen(modifier = modifier)

    SearchSuggestionsUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    SearchSuggestionsUiState.Success -> {
      if (suggestionList.isEmpty()) {
        NotFoundScreen(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      } else {
        SuggestionsList(
          suggestionList = suggestionList,
          onSelectedSuggestion = onSelectedSuggestion,
          modifier = modifier
        )
      }
    }
  }
}

@Composable
fun MangaListResults(
  query: String,
  resultsUiState: SearchResultsUiState,
  onSelectedManga: (String) -> Unit,
  onLoadSearchMangaListNextPage: () -> Unit,
  onRetryLoadSearchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (resultsUiState) {
    SearchResultsUiState.FirstPageLoading -> ListLoadingScreen(modifier = modifier)

    SearchResultsUiState.FirstPageError -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is SearchResultsUiState.Content -> {
      val mangaList = resultsUiState.items
      val nextPageState = resultsUiState.nextPageState

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
              SearchMangaListNextPageState.LOADING -> NextPageLoadingScreen(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 12.dp)
              )

              SearchMangaListNextPageState.ERROR -> LoadPageErrorScreen(
                message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                onRetry = onRetryLoadSearchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              )

              SearchMangaListNextPageState.IDLE -> LoadMoreText(
                onLoadMore = onLoadSearchMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )

              SearchMangaListNextPageState.NO_MORE_ITEMS -> AllItemLoadedText(
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
private fun SuggestionsList(
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    items(suggestionList) { suggestion ->
      SuggestionItem(
        suggestion = suggestion,
        onSelectedSuggestion = onSelectedSuggestion,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun SuggestionItem(
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