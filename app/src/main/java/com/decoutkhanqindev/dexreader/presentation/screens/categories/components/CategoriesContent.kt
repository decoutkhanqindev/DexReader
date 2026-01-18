package com.decoutkhanqindev.dexreader.presentation.screens.categories.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoryGroup
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  onSelectedCategory: (String, String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    CategoriesUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
        )
      }
    }

    is CategoriesUiState.Success -> {
      var expandedGroup by rememberSaveable { mutableStateOf<String?>(null) }

      LazyColumn(
        modifier = modifier
          .padding(top = 8.dp)
          .padding(horizontal = 4.dp)
      ) {
        item {
          CategoryGroupSection(
            isExpanded = expandedGroup == CategoryGroup.Genre.name,
            onExpandClick = {
              expandedGroup = if (expandedGroup == CategoryGroup.Genre.name) null
              else CategoryGroup.Genre.name
            },
            group = CategoryGroup.Genre.name,
            categoryList = uiState.genreList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryGroupSection(
            isExpanded = expandedGroup == CategoryGroup.Theme.name,
            onExpandClick = {
              expandedGroup = if (expandedGroup == CategoryGroup.Theme.name) null
              else CategoryGroup.Theme.name
            },
            group = CategoryGroup.Theme.name,
            categoryList = uiState.themeList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryGroupSection(
            isExpanded = expandedGroup == CategoryGroup.Format.name,
            onExpandClick = {
              expandedGroup = if (expandedGroup == CategoryGroup.Format.name) null
              else CategoryGroup.Format.name
            },
            group = CategoryGroup.Format.name,
            categoryList = uiState.formatList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryGroupSection(
            isExpanded = expandedGroup == CategoryGroup.Content.name,
            onExpandClick = {
              expandedGroup = if (expandedGroup == CategoryGroup.Content.name) null
              else CategoryGroup.Content.name
            },
            group = CategoryGroup.Content.name,
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

