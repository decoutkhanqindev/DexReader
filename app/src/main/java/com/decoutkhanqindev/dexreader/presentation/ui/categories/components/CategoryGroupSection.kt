package com.decoutkhanqindev.dexreader.presentation.ui.categories.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.domain.model.Category
import java.util.Locale

@Composable
fun CategoryGroupSection(
  categoryList: List<Category>,
  onSelectedCategory: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val group = categoryList.first().group.capitalize(locale = Locale.US)
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier) {
    CategoryHeader(
      group = group,
      isExpanded = isExpanded,
      onExpandClick = { isExpanded = !isExpanded },
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