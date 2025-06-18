package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SuggestionList(
  suggestionList: List<String>,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    itemsIndexed(
      items = suggestionList,
      key = { index, suggestion -> "${suggestionList[index]}_$index" }
    ) { index, suggestion ->
      SuggestionItem(
        suggestion = suggestion,
        onSelectedSuggestion = onSelectedSuggestion,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
