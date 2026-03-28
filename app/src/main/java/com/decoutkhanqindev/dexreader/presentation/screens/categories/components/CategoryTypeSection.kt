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
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import kotlinx.collections.immutable.ImmutableList

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