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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.sections.SectionHeader
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.screens.favorites.components.FavoriteMangaItem
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileFavoritesSection(
  uiState: BasePaginationUiState<FavoriteMangaModel>,
  modifier: Modifier = Modifier,
  onMangaClick: (String) -> Unit,
  onRetry: () -> Unit,
  onMoreClick: (() -> Unit)? = null,
) {
  Column(modifier = modifier) {
    SectionHeader(
      icon = Icons.Default.Favorite,
      title = stringResource(R.string.favorite_menu_item),
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
        .height(270.dp),
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

        is BasePaginationUiState.Content<FavoriteMangaModel> -> {
          val topFavoriteMangaList = uiState.currentList.take(TOP_ITEM_COUNT)

          if (topFavoriteMangaList.isEmpty()) {
            IdleScreen(
              message = stringResource(R.string.you_haven_t_added_any_favorite_manga_yet),
              modifier = Modifier.fillMaxSize()
            )
          } else {
            LazyRow(
              modifier = Modifier.fillMaxSize(),
              horizontalArrangement = Arrangement.spacedBy(2.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              items(
                items = topFavoriteMangaList,
                key = FavoriteMangaModel::id
              ) {
                FavoriteMangaItem(
                  manga = it,
                  onSelectedManga = onMangaClick,
                  modifier = Modifier
                    .padding(4.dp)
                    .width(194.dp)
                    .height(250.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

private const val TOP_ITEM_COUNT = 5

private val previewFavoriteList = persistentListOf(
  FavoriteMangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    author = "Eiichiro Oda",
    status = MangaStatusValue.ON_GOING,
    rating = "9.1",
    follows = "2.3M",
  ),
  FavoriteMangaModel(
    id = "2",
    title = "Fullmetal Alchemist",
    coverUrl = "",
    author = "Hiromu Arakawa",
    status = MangaStatusValue.COMPLETED,
    rating = "9.2",
    follows = "1.1M",
  ),
)

@Preview
@Composable
private fun ProfileFavoritesSectionContentPreview() {
  DexReaderTheme {
    ProfileFavoritesSection(
      uiState = BasePaginationUiState.Content(
        currentList = previewFavoriteList,
        nextPageState = BaseNextPageState.IDLE
      ),
      modifier = Modifier.fillMaxWidth(),
      onMangaClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun ProfileFavoritesSectionEmptyPreview() {
  DexReaderTheme {
    ProfileFavoritesSection(
      uiState = BasePaginationUiState.Content(
        currentList = persistentListOf(),
        nextPageState = BaseNextPageState.NO_MORE_ITEMS
      ),
      modifier = Modifier.fillMaxWidth(),
      onMangaClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun ProfileFavoritesSectionErrorPreview() {
  DexReaderTheme {
    ProfileFavoritesSection(
      uiState = BasePaginationUiState.FirstPageError(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxWidth(),
      onMangaClick = {},
      onMoreClick = {},
      onRetry = {}
    )
  }
}
