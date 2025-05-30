package com.decoutkhanqindev.dexreader.presentation.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.search.components.SearchBar
import com.decoutkhanqindev.dexreader.presentation.ui.search.components.SearchContent


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
      SearchBar(
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
        suggestionsUiState = suggestionsUiState,
        resultsUiState = resultsUiState,
        isExpanded = isExpanded,
        suggestionList = suggestionList,
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



