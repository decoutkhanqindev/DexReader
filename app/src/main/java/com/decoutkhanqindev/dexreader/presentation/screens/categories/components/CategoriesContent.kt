package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  modifier: Modifier = Modifier,
  onItemClick: (String, String) -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(true) }

  LaunchedEffect(uiState) {
    if (uiState is CategoriesUiState.Error) isShowErrorDialog = true
  }

  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    is CategoriesUiState.Error -> {
      if (isShowErrorDialog) {
        AlertDialog(
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
      var expandedType by remember { mutableStateOf<CategoryTypeValue?>(null) }
      val categoryTypes = remember(uiState) {
        uiState.categoryMap.keys.toPersistentList()
      }

      Column(
        modifier = modifier
          .padding(top = 8.dp)
          .padding(horizontal = 4.dp)
          .verticalScroll(rememberScrollState())
      ) {
        categoryTypes.forEach {
          CategoryTypeSection(
            isExpanded = expandedType == it,
            type = it,
            items = uiState.categoryMap[it] ?: persistentListOf(),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp),
            onExpandClick = { expandedType = if (expandedType == it) null else it },
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
  DexReaderTheme {
    CategoriesContent(
      uiState = CategoriesUiState.Loading,
      modifier = Modifier.fillMaxSize(),
      onItemClick = { _, _ -> },
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun CategoriesContentErrorPreview() {
  DexReaderTheme {
    CategoriesContent(
      uiState = CategoriesUiState.Error(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxSize(),
      onItemClick = { _, _ -> },
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun CategoriesContentSuccessPreview() {
  DexReaderTheme {
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
}

