package com.decoutkhanqindev.dexreader.presentation.ui.categories.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  onSelectedCategory: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    CategoriesUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is CategoriesUiState.Success -> {
      var expandedGroup by rememberSaveable { mutableStateOf<String?>(null) }
      val genre = uiState.genreList.first().group.capitalize(java.util.Locale.US)
      val theme = uiState.themeList.first().group.capitalize(java.util.Locale.US)
      val format = uiState.formatList.first().group.capitalize(java.util.Locale.US)
      val content = uiState.contentList.first().group.capitalize(java.util.Locale.US)

      Box(modifier = modifier) {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 4.dp)
        ) {
          item {
            CategoryGroupSection(
              isExpanded = expandedGroup == genre,
              onExpandClick = {
                expandedGroup = if (expandedGroup == genre) null else genre
              },
              group = genre,
              categoryList = uiState.genreList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              isExpanded = expandedGroup == theme,
              onExpandClick = {
                expandedGroup = if (expandedGroup == theme) null else theme
              },
              group = theme,
              categoryList = uiState.themeList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              isExpanded = expandedGroup == format,
              onExpandClick = {
                expandedGroup = if (expandedGroup == format) null else format
              },
              group = format,
              categoryList = uiState.formatList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              isExpanded = expandedGroup == content,
              onExpandClick = {
                expandedGroup = if (expandedGroup == content) null else content
              },
              group = content,
              categoryList = uiState.contentList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }
        }
      }
    }
  }
}

