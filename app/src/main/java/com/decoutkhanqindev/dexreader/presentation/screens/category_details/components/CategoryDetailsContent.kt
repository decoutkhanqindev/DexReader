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
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortOrderValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.CategoryDetailsCriteriaUiState
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter.FilterBottomSheet
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort.SortBottomSheet
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CategoryDetailsContent(
  detailsUiState: BasePaginationUiState<MangaModel>,
  criteriaUiState: CategoryDetailsCriteriaUiState,
  isSortBottomSheetVisible: Boolean,
  onSortSheetDismiss: () -> Unit,
  onSortApplyClick: (
    sortCriteria: MangaSortCriteriaValue,
    sortOrder: MangaSortOrderValue,
  ) -> Unit,
  isFilterBottomSheetVisible: Boolean,
  onFilterSheetDismiss: () -> Unit,
  onFilterApplyClick: (
    statusFilter: ImmutableList<MangaStatusValue>,
    contentRatingFilter: ImmutableList<MangaContentRatingValue>,
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
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
          title = stringResource(detailsUiState.error.messageRes),
          onDismissClick = { isShowErrorDialog = false },
        )
      }
    }

    is BasePaginationUiState.Content<MangaModel> -> {
      val mangaList = detailsUiState.currentList
      val nextPageState = detailsUiState.nextPageState

      VerticalGridMangaList(
        items = mangaList,
        onItemClick = { onMangaClick(it.id) },
        loadMoreContent = {
          when (nextPageState) {
            BaseNextPageState.LOADING -> NextPageLoadingIndicator(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )

            BaseNextPageState.ERROR -> LoadPageErrorMessage(
              message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
              onRetryClick = onRetryFetchMangaListNextPage,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
            )

            BaseNextPageState.IDLE -> LoadMoreMessage(
              onClick = onFetchMangaListNextPage,
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

private val previewCriteriaState = CategoryDetailsCriteriaUiState()

private val previewMangaList = persistentListOf(
  MangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    description = "A pirate adventure.",
    author = "Oda",
    artist = "Oda",
    categories = persistentListOf(CategoryModel(id = "g1", title = "Action")),
    status = MangaStatusValue.ON_GOING,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1997",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "1110",
    updatedAt = "2024-01-01",
  ),
  MangaModel(
    id = "2",
    title = "Naruto",
    coverUrl = "",
    description = "A ninja story.",
    author = "Kishimoto",
    artist = "Kishimoto",
    categories = persistentListOf(CategoryModel(id = "g2", title = "Adventure")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1999",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "700",
    updatedAt = "2014-11-10",
  ),
)

@Preview
@Composable
private fun CategoryDetailsContentFirstPageLoadingPreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.FirstPageLoading,
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}

@Preview
@Composable
private fun CategoryDetailsContentFirstPageErrorPreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.FirstPageError(FeatureError.NetworkUnavailable),
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}

@Preview
@Composable
private fun CategoryDetailsContentIdlePreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.Content(
      currentList = previewMangaList,
      nextPageState = BaseNextPageState.IDLE
    ),
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}

@Preview
@Composable
private fun CategoryDetailsContentNextPageLoadingPreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.Content(
      currentList = previewMangaList,
      nextPageState = BaseNextPageState.LOADING
    ),
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}

@Preview
@Composable
private fun CategoryDetailsContentNextPageErrorPreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.Content(
      currentList = previewMangaList,
      nextPageState = BaseNextPageState.ERROR
    ),
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}

@Preview
@Composable
private fun CategoryDetailsContentNoMoreItemsPreview() {
  CategoryDetailsContent(
    detailsUiState = BasePaginationUiState.Content(
      currentList = previewMangaList,
      nextPageState = BaseNextPageState.NO_MORE_ITEMS
    ),
    criteriaUiState = previewCriteriaState,
    isSortBottomSheetVisible = false,
    onSortSheetDismiss = {},
    onSortApplyClick = { _, _ -> },
    isFilterBottomSheetVisible = false,
    onFilterSheetDismiss = {},
    onFilterApplyClick = { _, _ -> },
    onMangaClick = {},
    onFetchMangaListNextPage = {},
    onRetryFetchMangaListNextPage = {},
    onRetry = {},
    modifier = Modifier.fillMaxSize()
  )
}