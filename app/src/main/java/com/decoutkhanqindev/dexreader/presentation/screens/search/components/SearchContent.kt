package com.decoutkhanqindev.dexreader.presentation.screens.search.components


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SuggestionsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.results.ResultsSection
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions.SuggestionsSection
import kotlinx.collections.immutable.ImmutableList


@Composable
fun SearchContent(
  query: String,
  suggestionsUiState: SuggestionsUiState,
  resultsUiState: BasePaginationUiState<MangaModel>,
  isExpanded: Boolean,
  suggestionList: ImmutableList<String>,
  modifier: Modifier = Modifier,
  onSelectedSuggestion: (String) -> Unit,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
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
      modifier = modifier,
    ) { onSelectedSuggestion(it) }
  } else {
    ResultsSection(
      query = query,
      resultsUiState = resultsUiState,
      modifier = modifier,
      onSelectedManga = onSelectedManga,
      onFetchMangaListNextPage = onFetchMangaListNextPage,
      onRetryFetchMangaListNextPage = onRetryFetchMangaListNextPage,
      onRetry = onRetry,
    )
  }
}