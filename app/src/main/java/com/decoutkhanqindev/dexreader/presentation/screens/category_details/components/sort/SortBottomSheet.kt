package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortOrderValue
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
  onDismiss: () -> Unit,
  criteriaState: CategoryDetailsCriteriaUiState,
  onApplyClick: (
    sortCriteria: MangaSortCriteriaValue,
    sortOrder: MangaSortOrderValue,
  ) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedCriteria by remember { mutableStateOf(criteriaState.sortCriteria) }
  var selectedOrder by remember { mutableStateOf(criteriaState.sortOrder) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    modifier = modifier
  ) {
    Text(
      text = stringResource(R.string.sort_options),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    )

    VerticalGridSortCriteriaList(
      selectedItem = selectedCriteria,
      onItemClick = { selectedCriteria = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    )

    SortOrderOptions(
      selectedItem = selectedOrder,
      onItemClick = { selectedOrder = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp)
    )

    ActionButton(
      backgroundColor = MaterialTheme.colorScheme.primary,
      onClick = {
        onApplyClick(selectedCriteria, selectedOrder)
        onDismiss()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp)
    ) {
      Text(
        text = stringResource(R.string.apply),
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SortBottomSheetPreview() {
  DexReaderTheme {
    SortBottomSheet(
      onDismiss = {},
      criteriaState = CategoryDetailsCriteriaUiState(),
      onApplyClick = { _, _ -> }
    )
  }
}
