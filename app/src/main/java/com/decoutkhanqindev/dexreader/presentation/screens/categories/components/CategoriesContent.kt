package com.decoutkhanqindev.dexreader.presentation.screens.categories.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeUiModel
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  onCategoryClick: (String, String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    is CategoriesUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(uiState.error.messageRes),
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
          restore = { CategoryTypeUiModel.valueOf(it) }
        )
      ) { mutableStateOf<CategoryTypeUiModel?>(null) }

      LazyColumn(
        modifier = modifier
          .padding(top = 8.dp)
          .padding(horizontal = 4.dp)
      ) {
        items(
          items = uiState.categoryMap.keys.toList(),
          key = { it.name }
        ) { type ->
          CategoryTypeSection(
            isExpanded = expandedType == type,
            onExpandClick = { expandedType = if (expandedType == type) null else type },
            type = type,
            categoryList = uiState.categoryMap[type] ?: emptyList(),
            onCategoryClick = onCategoryClick,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          )
        }
      }
    }
  }
}

