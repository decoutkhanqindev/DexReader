package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SuggestionList(
  suggestionList: ImmutableList<String>,
  modifier: Modifier = Modifier,
  onSelectedSuggestion: (String) -> Unit,
) {
  LazyColumn(modifier = modifier) {
    itemsIndexed(
      items = suggestionList,
      key = { index, suggestion -> "${suggestionList[index]}_$index" }
    ) { index, suggestion ->
      SuggestionItem(
        suggestion = suggestion,
        modifier = Modifier.fillMaxWidth(),
      ) { onSelectedSuggestion(it) }
    }
  }
}
