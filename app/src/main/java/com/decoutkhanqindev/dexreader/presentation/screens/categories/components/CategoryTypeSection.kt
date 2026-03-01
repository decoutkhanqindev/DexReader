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
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.presentation.model.CategoryTypeOption

@Composable
fun CategoryTypeSection(
  isExpanded: Boolean,
  onExpandClick: () -> Unit,
  type: CategoryTypeOption,
  categoryList: List<Category>,
  onSelectedCategory: (String, String) -> Unit,
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
        categoryList = categoryList,
        onSelectedCategory = onSelectedCategory,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}