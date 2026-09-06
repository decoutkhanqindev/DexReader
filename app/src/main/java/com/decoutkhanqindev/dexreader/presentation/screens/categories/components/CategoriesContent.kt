package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoryCoverUiState
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoryListUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
  categoryListUiState: CategoryListUiState,
  categoryCoverUiState: ImmutableMap<String, CategoryCoverUiState>,
  modifier: Modifier = Modifier,
  onCategoryClick: (categoryId: String, title: String, description: String) -> Unit,
  onLoadCover: (String) -> Unit,
  onRefresh: () -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  SideEffect(categoryListUiState) {
    if (categoryListUiState is CategoryListUiState.Error) isShowErrorDialog = true
  }

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = false,
    onRefresh = onRefresh,
    modifier = modifier,
  ) {
    when (categoryListUiState) {
      CategoryListUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is CategoryListUiState.Error -> {
        if (isShowErrorDialog) {
          AlertDialog(
            title = stringResource(categoryListUiState.error.messageRes),
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      is CategoryListUiState.Success -> {
        val categoryMap = categoryListUiState.categoryMap
        val lazyGridState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()
        val categoriesSize = remember(categoryMap) { categoryMap.values.sumOf { it.size } }

        Box(modifier = Modifier.fillMaxSize()) {
          CategoriesGrid(
            gridState = { lazyGridState },
            categoryMap = categoryMap,
            categoryCoverUiState = categoryCoverUiState,
            modifier = Modifier.fillMaxSize(),
            onCategoryClick = onCategoryClick,
            onLoadCover = onLoadCover,
          )

          MoveToTopButton(
            itemsSize = categoriesSize,
            gridState = { lazyGridState },
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(end = 16.dp, bottom = 78.dp)
          ) {
            coroutineScope.launch {
              lazyGridState.animateScrollToItem(0)
            }
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun CategoriesContentSuccessPreview() {
  DexReaderTheme {
    CategoriesContent(
      categoryListUiState = CategoryListUiState.Success(
        categoryMap = persistentMapOf(
          CategoryTypeValue.GENRE to persistentListOf(
            CategoryModel(id = "g1", title = "Action"),
            CategoryModel(id = "g2", title = "Romance"),
            CategoryModel(id = "g3", title = "Comedy"),
            CategoryModel(id = "g4", title = "Fantasy"),
          ),
          CategoryTypeValue.THEME to persistentListOf(
            CategoryModel(id = "t1", title = "Isekai"),
            CategoryModel(id = "t2", title = "School Life"),
          ),
        ),
      ),
      categoryCoverUiState = persistentMapOf(
        "g1" to CategoryCoverUiState.Success(coverUrl = ""),
        "g2" to CategoryCoverUiState.Loading,
        "g3" to CategoryCoverUiState.Success(coverUrl = ""),
      ),
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
      onRefresh = {},
      onRetry = {},
    )
  }
}

@Preview
@Composable
private fun CategoriesContentLoadingPreview() {
  DexReaderTheme {
    CategoriesContent(
      categoryListUiState = CategoryListUiState.Loading,
      categoryCoverUiState = persistentMapOf(),
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
      onRefresh = {},
      onRetry = {},
    )
  }
}

@Preview
@Composable
private fun CategoriesContentErrorPreview() {
  DexReaderTheme {
    CategoriesContent(
      categoryListUiState = CategoryListUiState.Error(FeatureError.NetworkUnavailable),
      categoryCoverUiState = persistentMapOf(),
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
      onRefresh = {},
      onRetry = {},
    )
  }
}
