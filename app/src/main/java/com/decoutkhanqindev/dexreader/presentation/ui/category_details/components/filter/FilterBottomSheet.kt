package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.filter

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
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions.ApplyButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
  onDismiss: () -> Unit,
  criteriaState: CategoryDetailsCriteriaUiState,
  onApplyClick: (
    statusValueIds: List<String>,
    contentRatingValueIds: List<String>
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var selectedStatusValueIds by rememberSaveable {
    mutableStateOf(criteriaState.statusValueIds)
  }
  var selectedContentRatingValueIds by rememberSaveable {
    mutableStateOf(criteriaState.contentRatingValueIds)
  }

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss,
    modifier = modifier
  ) {
    Text(
      text = stringResource(R.string.filter_options),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 16.dp)
    )

    VerticalGridFilterCriteriaList(
      selectedStatusValueIds = selectedStatusValueIds,
      onSelectedStatusOptions = { selectedStatusValueIds = it },
      selectedContentRatingValueIds = selectedContentRatingValueIds,
      onSelectedContentRatingOptions = { selectedContentRatingValueIds = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 24.dp)
    )

    ApplyButton(
      onApply = {
        onApplyClick(selectedStatusValueIds, selectedContentRatingValueIds)
        onDismiss()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp)
    )
  }
}