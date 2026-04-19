package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaCategoryList(
  items: ImmutableList<CategoryModel>,
  modifier: Modifier = Modifier,
  onItemClick: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(items, key = CategoryModel::id) { category ->
        MangaCategoryItem(
          item = category,
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        ) { categoryId, categoryTitle ->
          onItemClick(categoryId, categoryTitle)
        }
      }
    }
  }
}

@Preview
@Composable
private fun MangaCategoryListPreview() {
  DexReaderTheme {
    MangaCategoryList(
      items = persistentListOf(
        CategoryModel(id = "g1", title = "Action"),
        CategoryModel(id = "g2", title = "Adventure"),
        CategoryModel(id = "g3", title = "Comedy"),
        CategoryModel(id = "g4", title = "Fantasy"),
      ),
      modifier = Modifier.fillMaxWidth(),
      onItemClick = { _, _ -> }
    )
  }
}