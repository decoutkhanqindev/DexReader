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
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.filter.FilterBottomSheet
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.sort.SortBottomSheet

@Composable
fun CategoryDetailsContent(
  categoryDetailsUiState: CategoryDetailsUiState,
  categoryCriteriaUiState: CategoryDetailsCriteriaUiState,
  onSortApplyClick: (
    criteriaId: String,
    orderId: String
  ) -> Unit,
  onFilterApplyClick: (
    statusValueIds: List<String>,
    contentRatingValueIds: List<String>
  ) -> Unit,
  onSelectedManga: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isSortSheetVisible by rememberSaveable { mutableStateOf(false) }
  var isFilterSheetVisible by rememberSaveable { mutableStateOf(true) }

  if (isSortSheetVisible) {
    SortBottomSheet(
      onDismiss = { isSortSheetVisible = false },
      criteriaState = categoryCriteriaUiState,
      onApplyClick = { criteriaId, orderId ->
        onSortApplyClick(criteriaId, orderId)
        isSortSheetVisible = false
      },
      modifier = Modifier.fillMaxWidth()
    )
  }

  if (isFilterSheetVisible) {
    FilterBottomSheet(
      onDismiss = { isFilterSheetVisible = false },
      criteriaState = categoryCriteriaUiState,
      onApplyClick = { statusValueIds, contentRatingValueIds ->
        onFilterApplyClick(statusValueIds, contentRatingValueIds)
        isFilterSheetVisible = false
      },
      modifier = Modifier.fillMaxWidth()
    )
  }
}