package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoriesGridContentType
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoryCoverUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.TestTags
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun CategoriesGrid(
  gridState: () -> LazyGridState,
  categoryMap: ImmutableMap<CategoryTypeValue, ImmutableList<CategoryModel>>,
  categoryCoverUiState: ImmutableMap<String, CategoryCoverUiState>,
  modifier: Modifier = Modifier,
  onCategoryClick: (categoryId: String, title: String, description: String) -> Unit,
  onLoadCover: (String) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier.testTag(TestTags.CATEGORIES_GRID),
    state = gridState(),
    contentPadding = PaddingValues(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 82.dp),
  ) {
    categoryMap.forEach { (type, categories) ->
      if (categories.isNotEmpty()) {
        item(
          key = "header_${type.name}",
          span = { GridItemSpan(maxLineSpan) },
          contentType = CategoriesGridContentType.HEADER,
        ) {
          Text(
            text = stringResource(type.nameRes),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
          )
        }

        items(
          items = categories,
          key = { it.id },
          contentType = { CategoriesGridContentType.CARD },
        ) { category ->
          CategoryCard(
            category = category,
            coverState = categoryCoverUiState[category.id] ?: CategoryCoverUiState.Loading,
            modifier = Modifier
              .padding(4.dp)
              .fillMaxWidth()
              .height(250.dp),
            onCategoryClick = onCategoryClick,
            onLoadCover = onLoadCover,
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun CategoriesGridPreview() {
  DexReaderTheme {
    val lazyGridState = rememberLazyGridState()

    CategoriesGrid(
      gridState = { lazyGridState },
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
      categoryCoverUiState = persistentMapOf(
        "g1" to CategoryCoverUiState.Success(coverUrl = ""),
        "g2" to CategoryCoverUiState.Loading,
        "g3" to CategoryCoverUiState.Success(coverUrl = ""),
      ),
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
    )
  }
}
