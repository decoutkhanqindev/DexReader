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
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeUiModel
import com.decoutkhanqindev.dexreader.presentation.model.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryTypeSection(
  isExpanded: Boolean,
  onExpandClick: () -> Unit,
  type: CategoryTypeUiModel,
  categoryList: ImmutableList<CategoryUiModel>,
  onCategoryClick: (String, String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    CategoryTypeHeader(
      type = type,
      isExpanded = isExpanded,
      onExpandClick = onExpandClick,
      modifier = Modifier.fillMaxWidth()
    )
    AnimatedVisibility(
      visible = isExpanded,
      enter = expandVertically() + fadeIn(),
      exit = shrinkVertically() + fadeOut()
    ) {
      CategoryList(
        items = categoryList,
        onItemClick = onCategoryClick,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}