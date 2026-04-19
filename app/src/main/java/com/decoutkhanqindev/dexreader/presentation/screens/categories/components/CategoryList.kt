package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryList(
  items: ImmutableList<CategoryModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String, String) -> Unit,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items.forEach {
      CategoryItem(
        item = it,
        modifier = Modifier.wrapContentWidth(),
      ) { id, name -> onItemClick(id, name) }
    }
  }
}

@Preview
@Composable
private fun CategoryListPreview() {
  DexReaderTheme {
    CategoryList(
      items = persistentListOf(
        CategoryModel(id = "1", title = "Action"),
        CategoryModel(id = "2", title = "Adventure"),
        CategoryModel(id = "3", title = "Comedy"),
        CategoryModel(id = "4", title = "Drama"),
        CategoryModel(id = "5", title = "Fantasy"),
        CategoryModel(id = "6", title = "Slice of Life"),
      ),
      modifier = Modifier.fillMaxWidth(),
      onItemClick = { _, _ -> }
    )
  }
}