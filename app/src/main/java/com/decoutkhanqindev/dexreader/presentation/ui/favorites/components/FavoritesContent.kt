package com.decoutkhanqindev.dexreader.presentation.ui.favorites.components

import androidx.compose.foundation.layout.fillMaxSize
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
import com.decoutkhanqindev.dexreader.data.mapper.toManga
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.ui.common.lists.manga.VerticalGridMangaList
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.ui.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.ui.favorites.FavoritesNextPageState
import com.decoutkhanqindev.dexreader.presentation.ui.favorites.FavoritesUiState

@Composable
fun FavoritesContent(
  uiState: FavoritesUiState,
  onSelectedManga: (String) -> Unit,
  onObserveFavoriteMangaListNextPage: () -> Unit,
  onRetryObserveFavoriteMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(false) }

  when (uiState) {
    FavoritesUiState.Idle -> Unit

    FavoritesUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    FavoritesUiState.FirstPageError -> {
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

    is FavoritesUiState.Content -> {
      val favoriteMangaList = uiState.favoriteMangaList
      val nextPageState = uiState.nextPageState

      if (favoriteMangaList.isEmpty()) {
        IdleScreen(
          message = stringResource(R.string.you_haven_t_added_any_favorite_manga_yet),
          modifier = Modifier.fillMaxSize()
        )
      } else {
        VerticalGridMangaList(
          mangaList = favoriteMangaList.map { it.toManga() },
          onSelectedManga = { onSelectedManga(it.id) },
          loadMoreContent = {
            when (nextPageState) {
              FavoritesNextPageState.LOADING -> NextPageLoadingIndicator(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 12.dp)
              )

              FavoritesNextPageState.ERROR -> LoadPageErrorMessage(
                message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                onRetry = onRetryObserveFavoriteMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              )

              FavoritesNextPageState.IDLE -> LoadMoreMessage(
                onLoadMore = onObserveFavoriteMangaListNextPage,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .padding(bottom = 12.dp)
              )

              FavoritesNextPageState.NO_MORE_ITEMS -> AllItemLoadedMessage(
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
}