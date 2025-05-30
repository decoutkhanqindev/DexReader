package com.decoutkhanqindev.dexreader.presentation.ui.search.components.suggestions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.search.SuggestionsUiState
import com.decoutkhanqindev.dexreader.presentation.ui.search.components.SearchNotFoundMessage

@Composable
fun SuggestionsSection(
  query: String,
  suggestionsUiState: SuggestionsUiState,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  when (suggestionsUiState) {
    SuggestionsUiState.Loading -> ListLoadingIndicator(modifier = modifier)

    SuggestionsUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = {},
      modifier = modifier
    )

    SuggestionsUiState.Success -> {
      if (suggestionList.isEmpty()) {
        SearchNotFoundMessage(
          message = stringResource(R.string.sorry_no_manga_found_with_title, query),
          modifier = modifier
        )
      } else {
        SuggestionList(
          suggestionList = suggestionList,
          onSelectedSuggestion = onSelectedSuggestion,
          modifier = modifier
        )
      }
    }
  }
}
