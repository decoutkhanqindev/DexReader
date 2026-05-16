package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun VerticalGridSortCriteriaList(
  selectedItem: MangaSortCriteriaValue,
  onItemClick: (MangaSortCriteriaValue) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    maxItemsInEachRow = 2,
    modifier = modifier
  ) {
    MangaSortCriteriaValue.entries.forEach { criteria ->
      SortCriteriaItem(
        isSelected = selectedItem == criteria,
        item = criteria,
        onClick = remember(criteria) { { onItemClick(criteria) } },
        modifier = Modifier
          .weight(1f)
          .padding(4.dp)
          .size(50.dp)
      )
    }
  }
}

@Preview
@Composable
private fun VerticalGridSortCriteriaListPreview() {
  DexReaderTheme {
    VerticalGridSortCriteriaList(
      selectedItem = MangaSortCriteriaValue.LATEST_UPDATE,
      onItemClick = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    )
  }
}
