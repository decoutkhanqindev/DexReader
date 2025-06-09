package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.sort

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.SubmitButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
  onDismiss: () -> Unit,
  criteriaState: CategoryDetailsCriteriaUiState,
  onApplyClick: (
    criteriaId: String,
    orderId: String
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var selectedCriteriaId by rememberSaveable {
    mutableStateOf(criteriaState.selectedSortCriteriaId)
  }
  var selectedOrderId by rememberSaveable {
    mutableStateOf(criteriaState.selectedSortOrderId)
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
      selectedCriteriaId = selectedCriteriaId,
      onSelectedItem = { selectedCriteriaId = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    )

    SortOrderOptions(
      selectedOrderId = selectedOrderId,
      onSelectedOption = { selectedOrderId = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp)
    )

    SubmitButton(
      title = stringResource(R.string.apply),
      onSubmitClick = {
        onApplyClick(selectedCriteriaId, selectedOrderId)
        onDismiss()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp)
    )
  }
}

