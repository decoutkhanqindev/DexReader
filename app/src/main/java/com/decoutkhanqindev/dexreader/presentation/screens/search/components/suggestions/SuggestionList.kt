package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    items(items = suggestionList, key = { it.hashCode() }) { suggestion ->
      SuggestionItem(
        suggestion = suggestion,
        modifier = Modifier.fillMaxWidth(),
        onSelectedSuggestion = onSelectedSuggestion
      )
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
