package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions.FilterButton
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions.SortButton

@Composable
fun CategoryDetailsBottomBar(
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  BottomAppBar(
    actions = {
      SortButton(
        onSortClick = onSortClick,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .size(48.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      FilterButton(
        onFilterClick = onFilterClick,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .size(48.dp)
      )
    },
    modifier = modifier
  )
}