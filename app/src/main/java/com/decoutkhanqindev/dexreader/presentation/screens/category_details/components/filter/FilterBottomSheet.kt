package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter

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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterOption
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
  onDismiss: () -> Unit,
  criteriaState: CategoryDetailsCriteriaUiState,
  onApplyClick: (
    statusFilter: List<MangaStatusFilterOption>,
    contentRatingFilter: List<MangaContentRatingFilterOption>,
  ) -> Unit,
  modifier: Modifier = Modifier,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var selectedStatusOptions by rememberSaveable(
    stateSaver = listSaver(
      save = { list -> list.map { it.name } },
      restore = { names -> names.map { MangaStatusFilterOption.valueOf(it) } }
    )
  ) {
    mutableStateOf(criteriaState.statusFilter)
  }
  var selectedContentRatingOptions by rememberSaveable(
    stateSaver = listSaver(
      save = { list -> list.map { it.name } },
      restore = { names -> names.map { MangaContentRatingFilterOption.valueOf(it) } }
    )
  ) {
    mutableStateOf(criteriaState.contentRatingFilter)
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
        .padding(bottom = 16.dp)
    )

    VerticalGridFilterCriteriaList(
      selectedStatusOptions = selectedStatusOptions,
      onSelectedStatusOptions = { selectedStatusOptions = it },
      selectedContentRatingOptions = selectedContentRatingOptions,
      onSelectedContentRatingOptions = { selectedContentRatingOptions = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp)
    )

    SubmitButton(
      title = stringResource(R.string.apply),
      onSubmitClick = {
        onApplyClick(selectedStatusOptions, selectedContentRatingOptions)
        onDismiss()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .padding(bottom = 8.dp)
    )
  }
}
