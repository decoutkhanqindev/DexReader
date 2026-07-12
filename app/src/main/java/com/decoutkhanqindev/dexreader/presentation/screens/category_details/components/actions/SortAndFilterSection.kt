package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton

@Composable
fun SortAndFilterSection(
  itemsSize: Int,
  gridState: () -> LazyGridState,
  modifier: Modifier = Modifier,
  onMoveToTopClick: () -> Unit,
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    MoveToTopButton(
      itemsSize = itemsSize,
      gridState = gridState,
      onClick = onMoveToTopClick,
      modifier = Modifier
        .align(Alignment.End)
        .padding(end = 16.dp)
    )

    SortAndFilterButtons(
      onSortClick = onSortClick,
      onFilterClick = onFilterClick,
      modifier = Modifier
        .fillMaxWidth()
        .blurBackground(
          topAlpha = 0f,
          bottomAlpha = 1f,
        )
        .padding(16.dp),
    )
  }
}