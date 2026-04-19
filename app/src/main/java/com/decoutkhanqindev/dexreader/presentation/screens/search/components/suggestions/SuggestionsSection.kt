package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.SuggestionsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.results.ResultsNotFoundMessage
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SuggestionsSection(
  query: String,
  suggestionsUiState: SuggestionsUiState,
  suggestionList: ImmutableList<String>,
  modifier: Modifier = Modifier,
  onSelectedSuggestion: (String) -> Unit,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (suggestionsUiState) {
    SuggestionsUiState.Loading -> LoadingScreen(modifier = modifier)

    is SuggestionsUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(suggestionsUiState.error.messageRes),
          onConfirmClick = { isShowErrorDialog = false },
          onDismissClick = { isShowErrorDialog = false },
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
          modifier = modifier,
        ) { onSelectedSuggestion(it) }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun SuggestionsSectionLoadingPreview() {
  DexReaderTheme {
    SuggestionsSection(
      query = "One",
      suggestionsUiState = SuggestionsUiState.Loading,
      suggestionList = persistentListOf(),
      modifier = Modifier.fillMaxSize(),
      onSelectedSuggestion = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SuggestionsSectionErrorPreview() {
  DexReaderTheme {
    SuggestionsSection(
      query = "One",
      suggestionsUiState = SuggestionsUiState.Error(),
      suggestionList = persistentListOf(),
      modifier = Modifier.fillMaxSize(),
      onSelectedSuggestion = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SuggestionsSectionEmptyPreview() {
  DexReaderTheme {
    SuggestionsSection(
      query = "xyzabc",
      suggestionsUiState = SuggestionsUiState.Success,
      suggestionList = persistentListOf(),
      modifier = Modifier.fillMaxSize(),
      onSelectedSuggestion = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SuggestionsSectionSuccessPreview() {
  DexReaderTheme {
    SuggestionsSection(
      query = "One",
      suggestionsUiState = SuggestionsUiState.Success,
      suggestionList = persistentListOf(
        "One Piece",
        "One Punch Man",
        "One Piece Episodio di Ace",
      ),
      modifier = Modifier.fillMaxSize(),
      onSelectedSuggestion = {},
    )
  }
}
