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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.ui.components.bar.SearchMangaBar
import com.decoutkhanqindev.dexreader.ui.components.content.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.ui.components.states.EmptyScreen
import com.decoutkhanqindev.dexreader.ui.components.states.ErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.LoadingScreen
import com.decoutkhanqindev.dexreader.ui.components.states.NotFoundScreen

@Composable
fun SearchScreen(
  onMangaClick: (String) -> Unit,
  onBackClick: () -> Unit,
  viewModel: SearchViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val query by viewModel.query.collectAsStateWithLifecycle()
  val isSuggestionsLoading by viewModel.isSuggestionsLoading.collectAsStateWithLifecycle()
  val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
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
          viewModel.searchManga(it)
          isExpanded = false
        },
        onBackClick = onBackClick,
      )
    },
    content = { innerPadding ->
      SearchContent(
        uiState = uiState,
        query = query,
        isExpanded = isExpanded,
        isSuggestionsLoading = isSuggestionsLoading,
        suggestions = suggestions,
        onSuggestionClick = {
          viewModel.onQueryChange(it)
          viewModel.searchManga(it)
          isExpanded = false
        },
        onMangaClick = onMangaClick,
        onRetryClick = { viewModel.searchManga(it) },
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      )
    }
  )
}

@Composable
fun SearchContent(
  uiState: SearchUiState,
  query: String,
  isExpanded: Boolean,
  isSuggestionsLoading: Boolean,
  suggestions: List<String>,
  onSuggestionClick: (String) -> Unit,
  onMangaClick: (String) -> Unit,
  onRetryClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  if (isExpanded) {
    ShowSuggestions(
      query = query,
      suggestions = suggestions,
      isSuggestionsLoading = isSuggestionsLoading,
      onSuggestionClick = onSuggestionClick,
      modifier = modifier
    )
  } else {
    ShowResults(
      uiState = uiState,
      query = query,
      onMangaClick = onMangaClick,
      onRetryClick = onRetryClick,
      modifier = modifier
    )
  }
}

@Composable
private fun ShowSuggestions(
  query: String,
  suggestions: List<String>,
  isSuggestionsLoading: Boolean,
  onSuggestionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  when (isSuggestionsLoading) {
    true -> LoadingScreen(modifier = modifier)

    false -> {
      if (suggestions.isNotEmpty()) {
        SuggestionsList(
          suggestions = suggestions,
          onSuggestionClick = onSuggestionClick,
          modifier = modifier
        )
      } else {
        NotFoundScreen(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      }
    }
  }
}

@Composable
private fun ShowResults(
  uiState: SearchUiState,
  query: String,
  onMangaClick: (String) -> Unit,
  onRetryClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  when (uiState) {
    SearchUiState.Idle -> EmptyScreen(
      message = stringResource(R.string.search_your_manga_here),
      modifier = modifier
    )

    SearchUiState.Loading -> LoadingScreen(modifier = modifier)

    is SearchUiState.Success -> {
      val mangaList = uiState.results
      if (mangaList.isNotEmpty()) {
        VerticalGridMangaList(
          mangaList = uiState.results,
          onMangaClick = { onMangaClick(it.id) },
          modifier = modifier
        )
      } else {
        NotFoundScreen(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      }
    }

    SearchUiState.Error -> ErrorScreen(
      errorMessage = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetryClick = { onRetryClick(query) },
      modifier = modifier
    )
  }
}

@Composable
private fun SuggestionsList(
  suggestions: List<String>,
  onSuggestionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    items(suggestions) { suggestion ->
      SuggestionItem(
        suggestion = suggestion,
        onSuggestionClick = onSuggestionClick,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun SuggestionItem(
  suggestion: String,
  onSuggestionClick: (String) -> Unit,
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
    onClick = { onSuggestionClick(suggestion) },
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.search)
      )
    },
    modifier = modifier
  )
}