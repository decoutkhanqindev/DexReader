package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortAndFilterBottomBar(
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
      )
      Spacer(modifier = Modifier.width(8.dp))
      FilterButton(
        onFilterClick = onFilterClick,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      )
    },
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    modifier = modifier
  )
}