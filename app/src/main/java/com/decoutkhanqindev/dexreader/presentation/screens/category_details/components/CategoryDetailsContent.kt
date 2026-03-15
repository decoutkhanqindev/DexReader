package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderUiModel
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter.FilterBottomSheet
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort.SortBottomSheet
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CategoryDetailsContent(
  detailsUiState: BasePaginationUiState<MangaUiModel>,
  criteriaUiState: CategoryDetailsCriteriaUiState,
  isSortBottomSheetVisible: Boolean,
  onSortSheetDismiss: () -> Unit,
  onSortApplyClick: (
    sortCriteria: MangaSortCriteriaUiModel,
    sortOrder: MangaSortOrderUiModel,
  ) -> Unit,
  isFilterBottomSheetVisible: Boolean,
  onFilterSheetDismiss: () -> Unit,
  onFilterApplyClick: (
    statusFilter: ImmutableList<MangaStatusFilterUiModel>,
    contentRatingFilter: ImmutableList<MangaContentRatingFilterUiModel>,
  ) -> Unit,
  onMangaClick: (String) -> Unit,
  onFetchMangaListNextPage: () -> Unit,
  onRetryFetchMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  if (isSortBottomSheetVisible) {
    SortBottomSheet(
      onDismiss = onSortSheetDismiss,
      criteriaState = criteriaUiState,
      onApplyClick = { criteriaId, orderId ->
        onSortApplyClick(criteriaId, orderId)
      },
      modifier = Modifier.fillMaxWidth()
    )
  }

  if (isFilterBottomSheetVisible) {
    FilterBottomSheet(
      onDismiss = onFilterSheetDismiss,
      criteriaState = criteriaUiState,
      onApplyClick = { statusValueIds, contentRatingValueIds ->
        onFilterApplyClick(statusValueIds, contentRatingValueIds)
      },
      modifier = Modifier.fillMaxWidth()
    )
  }

  when (detailsUiState) {
    BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    is BasePaginationUiState.FirstPageError -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(detailsUiState.error.messageRes),
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
        )
      }
    }

    is BasePaginationUiState.Content<MangaUiModel> -> {
      val mangaList = detailsUiState.currentList
      val nextPageState = detailsUiState.nextPageState

      VerticalGridMangaList(
        mangaList = mangaList,
        onSelectedManga = { onMangaClick(it.id) },
        loadMoreContent = {
          when (nextPageState) {
            BaseNextPageState.LOADING -> NextPageLoadingIndicator(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )

            BaseNextPageState.ERROR -> LoadPageErrorMessage(
              message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
              onRetry = onRetryFetchMangaListNextPage,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
            )

            BaseNextPageState.IDLE -> LoadMoreMessage(
              onLoadMore = onFetchMangaListNextPage,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 12.dp)
            )

            BaseNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
              title = stringResource(R.string.all_mangas_loaded),
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 12.dp)
            )
          }
        },
        modifier = modifier
      )
    }
  }
}