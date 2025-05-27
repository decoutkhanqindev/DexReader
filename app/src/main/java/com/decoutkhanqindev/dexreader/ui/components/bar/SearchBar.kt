package com.decoutkhanqindev.dexreader.ui.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun SearchMangaBar(
  query: String,
  onQueryChange: (String) -> Unit,
  onSearch: (String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
    TextField(
      value = query,
      onValueChange = onQueryChange,
      placeholder = { Text(text = stringResource(R.string.search_manga_placeholder)) },
      singleLine = true,
      leadingIcon = {
        IconButton(onClick = onNavigateBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back)
          )
        }
      },
      trailingIcon = {
        if (query.isNotEmpty()) {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = stringResource(R.string.clear)
            )
          }
        } else {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search)
          )
        }
      },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
      shape = RoundedCornerShape(0.dp),
      colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
      ),
      textStyle = MaterialTheme.typography.titleMedium,
      modifier = Modifier
        .statusBarsPadding()
        .fillMaxWidth()
    )
  }
}

