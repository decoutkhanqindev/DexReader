package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryTypeSection(
  isExpanded: Boolean,
  type: CategoryTypeValue,
  items: ImmutableList<CategoryModel>,
  modifier: Modifier = Modifier,
  onExpandClick: () -> Unit,
  onItemClick: (String, String) -> Unit,
) {
  Column(modifier = modifier) {
    CategoryTypeHeader(
      type = type,
      isExpanded = isExpanded,
      modifier = Modifier.fillMaxWidth(),
    ) { onExpandClick() }
    AnimatedVisibility(
      visible = isExpanded,
      enter = expandVertically() + fadeIn(),
      exit = shrinkVertically() + fadeOut()
    ) {
      CategoryList(
        items = items,
        modifier = Modifier.fillMaxWidth(),
      ) { id, name -> onItemClick(id, name) }
    }
  }
}

private val previewCategoryItems = persistentListOf(
  CategoryModel(id = "1", title = "Action"),
  CategoryModel(id = "2", title = "Adventure"),
  CategoryModel(id = "3", title = "Comedy"),
  CategoryModel(id = "4", title = "Drama"),
  CategoryModel(id = "5", title = "Fantasy"),
)

@Preview
@Composable
private fun CategoryTypeSectionCollapsedPreview() {
  DexReaderTheme {
    CategoryTypeSection(
      isExpanded = false,
      type = CategoryTypeValue.GENRE,
      items = previewCategoryItems,
      modifier = Modifier.fillMaxWidth(),
      onExpandClick = {},
      onItemClick = { _, _ -> }
    )
  }
}

@Preview
@Composable
private fun CategoryTypeSectionExpandedPreview() {
  DexReaderTheme {
    CategoryTypeSection(
      isExpanded = true,
      type = CategoryTypeValue.GENRE,
      items = previewCategoryItems,
      modifier = Modifier.fillMaxWidth(),
      onExpandClick = {},
      onItemClick = { _, _ -> }
    )
  }
}