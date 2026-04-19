package com.decoutkhanqindev.dexreader.presentation.screens.search.components.suggestions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R

@Composable
fun SuggestionItem(
  suggestion: String,
  modifier: Modifier = Modifier,
  onSelectedSuggestion: (String) -> Unit,
) {
  DropdownMenuItem(
    text = {
      Text(
        text = suggestion,
        fontWeight = FontWeight.Light,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.bodyLarge,
      )
    },
    onClick = { onSelectedSuggestion(suggestion) },
    modifier = modifier,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.search)
      )
    },
  )
}

@Preview(showBackground = true)
@Composable
private fun SuggestionItemPreview() {
  SuggestionItem(
    suggestion = "One Piece",
    onSelectedSuggestion = {},
  )
}