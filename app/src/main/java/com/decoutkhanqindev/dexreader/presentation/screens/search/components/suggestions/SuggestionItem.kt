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
import com.decoutkhanqindev.dexreader.R

@Composable
fun SuggestionItem(
  suggestion: String,
  onSelectedSuggestion: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  DropdownMenuItem(
    text = {
      Text(
        text = suggestion,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Light,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    onClick = { onSelectedSuggestion(suggestion) },
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.search)
      )
    },
    modifier = modifier
  )
}