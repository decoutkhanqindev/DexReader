package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionsButtonSection(
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    SortButton(
      onSortClick = onSortClick,
      modifier = Modifier.weight(1f)
    )
    FilterButton(
      onFilterClick = onFilterClick,
      modifier = Modifier.weight(1f)
    )
  }
}