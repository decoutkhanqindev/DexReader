package com.decoutkhanqindev.dexreader.presentation.screens.search.components.actions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
  query: String,
  modifier: Modifier = Modifier,
  onQueryChange: (String) -> Unit,
  onSearch: (String) -> Unit,
  onNavigateBack: () -> Unit,
) {
  CenterAlignedTopAppBar(
    title = {
      TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(
            text = stringResource(R.string.search_manga_placeholder),
            style = MaterialTheme.typography.bodyLarge
          )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
          unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
          focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
          unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
          cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
      )
    },
    modifier = modifier,
    navigationIcon = {
      IconButton(onClick = onNavigateBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(R.string.back)
        )
      }
    },
    actions = {
      if (query.isNotEmpty()) {
        IconButton(onClick = { onQueryChange("") }) {
          Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear)
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainer,
      scrolledContainerColor = Color.Unspecified,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      titleContentColor = Color.Unspecified,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
  )
}

@Preview
@Composable
private fun SearchBarEmptyPreview() {
  DexReaderTheme {
    SearchBar(
      query = "",
      onQueryChange = {},
      onSearch = {},
      onNavigateBack = {},
    )
  }
}

@Preview
@Composable
private fun SearchBarWithQueryPreview() {
  DexReaderTheme {
    SearchBar(
      query = "One Piece",
      onQueryChange = {},
      onSearch = {},
      onNavigateBack = {},
    )
  }
}

