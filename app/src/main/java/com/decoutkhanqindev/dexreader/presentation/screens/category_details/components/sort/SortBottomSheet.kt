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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderOption
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
  onDismiss: () -> Unit,
  criteriaState: CategoryDetailsCriteriaUiState,
  onApplyClick: (
    sortCriteria: MangaSortCriteriaOption,
    sortOrder: MangaSortOrderOption,
  ) -> Unit,
  modifier: Modifier = Modifier,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var selectedCriteria by rememberSaveable(
    stateSaver = Saver(
      save = { it.name },
      restore = { MangaSortCriteriaOption.valueOf(it) }
    )
  ) {
    mutableStateOf(criteriaState.sortCriteria)
  }
  var selectedOrder by rememberSaveable(
    stateSaver = Saver(
      save = { it.name },
      restore = { MangaSortOrderOption.valueOf(it) }
    )
  ) {
    mutableStateOf(criteriaState.sortOrder)
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
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
      selectedCriteria = selectedCriteria,
      onSelectedItem = { selectedCriteria = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    )

    SortOrderOptions(
      selectedOrder = selectedOrder,
      onSelectedOption = { selectedOrder = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp)
    )

    SubmitButton(
      title = stringResource(R.string.apply),
      onSubmitClick = {
        onApplyClick(selectedCriteria, selectedOrder)
        onDismiss()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp)
    )
  }
}
