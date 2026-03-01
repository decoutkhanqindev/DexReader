package com.decoutkhanqindev.dexreader.presentation.screens.categories.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeOption
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesUiState
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
      var expandedType by rememberSaveable(
        stateSaver = Saver(
          save = { it?.name },
          restore = { it.let { name -> CategoryTypeOption.valueOf(name) } }
        )
      ) { mutableStateOf<CategoryTypeOption?>(null) }

      LazyColumn(
        modifier = modifier
          .padding(top = 8.dp)
          .padding(horizontal = 4.dp)
      ) {
        item {
          CategoryTypeSection(
            isExpanded = expandedType == CategoryTypeOption.GENRE,
            onExpandClick = {
              expandedType = if (expandedType == CategoryTypeOption.GENRE) null
              else CategoryTypeOption.GENRE
            },
            type = CategoryTypeOption.GENRE,
            categoryList = uiState.genreList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryTypeSection(
            isExpanded = expandedType == CategoryTypeOption.THEME,
            onExpandClick = {
              expandedType = if (expandedType == CategoryTypeOption.THEME) null
              else CategoryTypeOption.THEME
            },
            type = CategoryTypeOption.THEME,
            categoryList = uiState.themeList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryTypeSection(
            isExpanded = expandedType == CategoryTypeOption.FORMAT,
            onExpandClick = {
              expandedType = if (expandedType == CategoryTypeOption.FORMAT) null
              else CategoryTypeOption.FORMAT
            },
            type = CategoryTypeOption.FORMAT,
            categoryList = uiState.formatList,
            onSelectedCategory = onSelectedCategory,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }

        item {
          CategoryTypeSection(
            isExpanded = expandedType == CategoryTypeOption.CONTENT,
            onExpandClick = {
              expandedType = if (expandedType == CategoryTypeOption.CONTENT) null
              else CategoryTypeOption.CONTENT
            },
            type = CategoryTypeOption.CONTENT,
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

