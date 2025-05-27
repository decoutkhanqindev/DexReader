package com.decoutkhanqindev.dexreader.ui.components.bar

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.decoutkhanqindev.dexreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMangaBar(
  query: String,
  onQueryChange: (String) -> Unit,
  onSearch: (String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  CenterAlignedTopAppBar(
    title = {
      TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
          Text(
            text = stringResource(R.string.search_manga_placeholder),
            style = MaterialTheme.typography.titleMedium
          )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
          unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
          focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
          unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
          focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
          unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
          focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
          cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        textStyle = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
      )
    },
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
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ),
    modifier = modifier
  )
}

