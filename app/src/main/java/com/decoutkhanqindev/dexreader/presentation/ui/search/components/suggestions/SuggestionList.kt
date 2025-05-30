package com.decoutkhanqindev.dexreader.presentation.ui.search.components.suggestions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SuggestionList(
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    items(suggestionList, key = { it }) { mangaSuggestion ->
      SuggestionItem(
        suggestion = mangaSuggestion,
        onSelectedSuggestion = onSelectedSuggestion,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
