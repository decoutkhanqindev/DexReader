package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components

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
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.filter.FilterBottomSheet
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.sort.SortBottomSheet
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.lists.manga.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage

@Composable
fun CategoryDetailsContent(
  categoryDetailsUiState: BasePaginationUiState<Manga>,
  categoryCriteriaUiState: CategoryDetailsCriteriaUiState,
  isSortBottomSheetVisible: Boolean,
  onSortSheetDismiss: () -> Unit,
  onSortApplyClick: (
    criteriaId: String,
    orderId: String
  ) -> Unit,
  isFilterBottomSheetVisible: Boolean,
  onFilterSheetDismiss: () -> Unit,
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
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  if (isSortBottomSheetVisible) {
    SortBottomSheet(
      onDismiss = onSortSheetDismiss,
      criteriaState = categoryCriteriaUiState,
      onApplyClick = { criteriaId, orderId ->
        onSortApplyClick(criteriaId, orderId)
      },
      modifier = Modifier.fillMaxWidth()
    )
  }

  if (isFilterBottomSheetVisible) {
    FilterBottomSheet(
      onDismiss = onFilterSheetDismiss,
      criteriaState = categoryCriteriaUiState,
      onApplyClick = { statusValueIds, contentRatingValueIds ->
        onFilterApplyClick(statusValueIds, contentRatingValueIds)
      },
      modifier = Modifier.fillMaxWidth()
    )
  }

  when (categoryDetailsUiState) {
    BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    BasePaginationUiState.FirstPageError -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
        )
      }
    }

    is BasePaginationUiState.Content<Manga> -> {
      val mangaList = categoryDetailsUiState.currentList
      val nextPageState = categoryDetailsUiState.nextPageState

      VerticalGridMangaList(
        mangaList = mangaList,
        onSelectedManga = { onSelectedManga(it.id) },
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