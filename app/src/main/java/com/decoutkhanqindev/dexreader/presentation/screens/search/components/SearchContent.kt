package com.decoutkhanqindev.dexreader.presentation.screens.search.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SuggestionsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.results.ResultsSection
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions.SuggestionsSection


@Composable
fun SearchContent(
  query: String,
  suggestionsUiState: SuggestionsUiState,
  resultsUiState: BasePaginationUiState<Manga>,
  isExpanded: Boolean,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
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
    SuggestionsSection(
      query = query,
      suggestionsUiState = suggestionsUiState,
      suggestionList = suggestionList,
      onSelectedSuggestion = onSelectedSuggestion,
      modifier = modifier
    )
  } else {
    ResultsSection(
      query = query,
      resultsUiState = resultsUiState,
      onSelectedManga = onSelectedManga,
      onFetchMangaListNextPage = onFetchMangaListNextPage,
      onRetryFetchMangaListNextPage = onRetryFetchMangaListNextPage,
      onRetry = onRetry,
      modifier = modifier
    )
  }
}