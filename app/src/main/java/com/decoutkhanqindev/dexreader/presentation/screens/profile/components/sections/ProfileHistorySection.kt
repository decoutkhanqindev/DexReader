package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.sections.SectionHeader
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileHistorySection(
  uiState: BasePaginationUiState<ReadingHistoryModel>,
  modifier: Modifier = Modifier,
  onContinueReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onMangaDetailsClick: (String) -> Unit,
  onRetry: () -> Unit,
  onMoreClick: (() -> Unit)? = null,
) {
  var selectedMangaId by remember { mutableStateOf<String?>(null) }
  var selectedChapterId by remember { mutableStateOf<String?>(null) }
  var selectedLastReadPage by remember { mutableStateOf<Int?>(null) }
  var isShowNavigateDialog by remember { mutableStateOf(false) }

  Column(modifier = modifier) {
    SectionHeader(
      icon = MenuValue.HISTORY.icon,
      title = stringResource(MenuValue.HISTORY.nameRes),
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = 16.dp,
          end = 16.dp,
          top = 8.dp,
          bottom = 4.dp
        ),
      onMoreClick = onMoreClick
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
      contentAlignment = Alignment.Center
    ) {
      when (uiState) {
        BasePaginationUiState.FirstPageLoading -> ListLoadingIndicator(
          modifier = Modifier.fillMaxWidth()
        )

        is BasePaginationUiState.FirstPageError -> LoadPageErrorMessage(
          message = stringResource(uiState.error.messageRes),
          onRetryClick = onRetry,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        )

        is BasePaginationUiState.Content<ReadingHistoryModel> -> {
          val topReadingHistoryList = uiState.currentList.take(TOP_ITEM_COUNT)

          if (topReadingHistoryList.isEmpty()) {
            IdleScreen(
              message = stringResource(R.string.you_have_no_reading_history_here),
              modifier = Modifier.fillMaxSize()
            )
          } else {
            LazyRow(
              modifier = Modifier.fillMaxSize(),
              horizontalArrangement = Arrangement.spacedBy(2.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              items(
                items = topReadingHistoryList,
                key = ReadingHistoryModel::id
              ) {
                ProfileHistoryItem(
                  readingHistory = it,
                  onSelectedReadingHistory = { mangaId, chapterId, lastReadPage ->
                    selectedMangaId = mangaId
                    selectedChapterId = chapterId
                    selectedLastReadPage = lastReadPage
                    isShowNavigateDialog = true
                  },
                  modifier = Modifier
                    .padding(4.dp)
                    .width(350.dp)
                    .height(200.dp)
                )
              }
            }
          }
        }
      }
    }
  }

  if (isShowNavigateDialog &&
    selectedMangaId != null &&
    selectedChapterId != null &&
    selectedLastReadPage != null
  ) {
    AlertDialog(
      onConfirmClick = {
        isShowNavigateDialog = false
        onMangaDetailsClick(selectedMangaId!!)
      },
      title = stringResource(R.string.view_details_or_continue),
      dismiss = stringResource(R.string.continue_reading),
      onDismissClick = {
        isShowNavigateDialog = false
        onContinueReadingClick(
          selectedChapterId!!,
          selectedLastReadPage!!,
          selectedMangaId!!
        )
      },
      onDismissOuterClick = { isShowNavigateDialog = false },
      confirm = stringResource(R.string.manga_details),
    )
  }
}

private const val TOP_ITEM_COUNT = 5

private val previewHistoryItems = persistentListOf(
  ReadingHistoryModel(
    id = "rh-001",
    mangaId = "m-001",
    mangaTitle = "One Piece",
    mangaCoverUrl = "",
    chapterId = "c-001",
    chapterTitle = "Romance Dawn",
    chapterNumber = "1",
    chapterVolume = "1",
    lastReadPage = 12,
    pageCount = 46,
    lastReadAt = "2 hours ago",
  ),
  ReadingHistoryModel(
    id = "rh-002",
    mangaId = "m-002",
    mangaTitle = "Naruto",
    mangaCoverUrl = "",
    chapterId = "c-002",
    chapterTitle = "Uzumaki Naruto!!",
    chapterNumber = "1",
    chapterVolume = "1",
    lastReadPage = 5,
    pageCount = 53,
    lastReadAt = "Yesterday",
  ),
)

@Preview
@Composable
private fun ProfileHistorySectionContentPreview() {
  DexReaderTheme {
    ProfileHistorySection(
      uiState = BasePaginationUiState.Content(
        currentList = previewHistoryItems,
        nextPageState = BaseNextPageState.IDLE
      ),
      modifier = Modifier.fillMaxWidth(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun ProfileHistorySectionEmptyPreview() {
  DexReaderTheme {
    ProfileHistorySection(
      uiState = BasePaginationUiState.Content(
        currentList = persistentListOf(),
        nextPageState = BaseNextPageState.NO_MORE_ITEMS
      ),
      modifier = Modifier.fillMaxWidth(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun ProfileHistorySectionErrorPreview() {
  DexReaderTheme {
    ProfileHistorySection(
      uiState = BasePaginationUiState.FirstPageError(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxWidth(),
      onContinueReadingClick = { _, _, _ -> },
      onMangaDetailsClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}
