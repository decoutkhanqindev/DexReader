package com.decoutkhanqindev.dexreader.presentation.ui.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Category

@Composable
fun CategoryList(
  categoryList: List<Category>,
  onSelectedCategory: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    categoryList.forEach { category ->
      CategoryItem(
        category = category,
        onSelectedCategory = onSelectedCategory,
        modifier = Modifier.wrapContentWidth()
      )
    }
  }
}