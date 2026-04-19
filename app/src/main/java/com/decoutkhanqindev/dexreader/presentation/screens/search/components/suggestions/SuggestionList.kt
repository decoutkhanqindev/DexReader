package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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

@Preview(showBackground = true)
@Composable
private fun SuggestionListPreview() {
  DexReaderTheme {
    SuggestionList(
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
