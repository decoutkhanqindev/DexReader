package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.CategoryDetailsUiState
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.sort.SortBottomSheetSection

@Composable
fun CategoryDetailsContent(
  categoryDetailsUiState: CategoryDetailsUiState,
  categoryCriteriaUiState: CategoryDetailsCriteriaUiState,
  onSortApplyClick: (
    criteriaId: String,
    orderId: String
  ) -> Unit,
  onFilterApplyClick: (String) -> Unit,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isSortSheetVisible by rememberSaveable { mutableStateOf(true) }
  var isFilterSheetVisible by rememberSaveable { mutableStateOf(false) }

  if (isSortSheetVisible) {
    SortBottomSheetSection(
      onDismiss = { isSortSheetVisible = false },
      criteriaState = categoryCriteriaUiState,
      onApplyClick = { criteriaId, orderId ->
        onSortApplyClick(criteriaId, orderId)
        isSortSheetVisible = false
      },
      modifier = Modifier.fillMaxWidth()
    )
  }
}