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
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  modifier: Modifier = Modifier,
  onItemClick: (String, String) -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    is CategoriesUiState.Error -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(uiState.error.messageRes),
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
          onDismissClick = { isShowErrorDialog = false },
        )
      }
    }

    is CategoriesUiState.Success -> {
      var expandedType by rememberSaveable(
        stateSaver = Saver(
          save = { it?.name },
          restore = { CategoryTypeValue.valueOf(it) }
        )
      ) { mutableStateOf<CategoryTypeValue?>(null) }

      LazyColumn(
        modifier = modifier
          .padding(top = 8.dp)
          .padding(horizontal = 4.dp)
      ) {
        items(
          items = uiState.categoryMap.keys.toPersistentList(),
          key = { it.name }
        ) { type ->
          CategoryTypeSection(
            isExpanded = expandedType == type,
            type = type,
            items = uiState.categoryMap[type] ?: persistentListOf(),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp),
            onExpandClick = { expandedType = if (expandedType == type) null else type },
            onItemClick = onItemClick,
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun CategoriesContentLoadingPreview() {
  CategoriesContent(
    uiState = CategoriesUiState.Loading,
    modifier = Modifier.fillMaxSize(),
    onItemClick = { _, _ -> },
    onRetry = {}
  )
}

@Preview
@Composable
private fun CategoriesContentErrorPreview() {
  CategoriesContent(
    uiState = CategoriesUiState.Error(FeatureError.NetworkUnavailable),
    modifier = Modifier.fillMaxSize(),
    onItemClick = { _, _ -> },
    onRetry = {}
  )
}

@Preview
@Composable
private fun CategoriesContentSuccessPreview() {
  CategoriesContent(
    uiState = CategoriesUiState.Success(
      categoryMap = persistentMapOf(
        CategoryTypeValue.GENRE to persistentListOf(
          CategoryModel(id = "1", title = "Action"),
          CategoryModel(id = "2", title = "Adventure"),
          CategoryModel(id = "3", title = "Comedy"),
          CategoryModel(id = "4", title = "Drama"),
          CategoryModel(id = "5", title = "Fantasy"),
        ),
        CategoryTypeValue.THEME to persistentListOf(
          CategoryModel(id = "6", title = "School Life"),
          CategoryModel(id = "7", title = "Isekai"),
          CategoryModel(id = "8", title = "Supernatural"),
        ),
        CategoryTypeValue.FORMAT to persistentListOf(
          CategoryModel(id = "9", title = "Long Strip"),
          CategoryModel(id = "10", title = "4-Koma"),
        )
      )
    ),
    modifier = Modifier.fillMaxSize(),
    onItemClick = { _, _ -> },
    onRetry = {}
  )
}

