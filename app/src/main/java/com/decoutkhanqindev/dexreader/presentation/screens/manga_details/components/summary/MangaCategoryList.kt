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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MangaCategoryList(
  categoryList: ImmutableList<CategoryModel>,
  modifier: Modifier = Modifier,
  onSelectedCategory: (
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
      items(categoryList, key = { it.id }) { category ->
        MangaCategoryItem(
          category = category,
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        ) { categoryId, categoryTitle -> onSelectedCategory(categoryId, categoryTitle) }
      }
    }
  }
}