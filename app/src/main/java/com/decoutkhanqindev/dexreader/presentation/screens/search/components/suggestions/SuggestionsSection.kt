package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SuggestionsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.results.ResultsNotFoundMessage

@Composable
fun SuggestionsSection(
  query: String,
  suggestionsUiState: SuggestionsUiState,
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (suggestionsUiState) {
    SuggestionsUiState.Loading -> LoadingScreen(modifier = modifier)

    SuggestionsUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = { isShowErrorDialog = false },
        )
      }
    }

    SuggestionsUiState.Success -> {
      if (suggestionList.isEmpty()) {
        ResultsNotFoundMessage(
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
