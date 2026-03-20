package com.decoutkhanqindev.dexreader.presentation.screens.favorites.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.NextPageLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.AllItemLoadedMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadMoreMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import kotlinx.coroutines.launch

@Composable
fun FavoritesContent(
  uiState: BasePaginationUiState<FavoriteMangaModel>,
  onSelectedManga: (String) -> Unit,
  onObserveFavoriteMangaListNextPage: () -> Unit,
  onRetryObserveFavoriteMangaListNextPage: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  when (uiState) {
    BasePaginationUiState.FirstPageLoading -> LoadingScreen(modifier = modifier)

    is BasePaginationUiState.FirstPageError -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(uiState.error.messageRes),
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = {
            isShowErrorDialog = false
            onRetry()
          },
        )
      }
    }

    is BasePaginationUiState.Content<FavoriteMangaModel> -> {
      val favoriteMangaList = uiState.currentList
      val nextPageState = uiState.nextPageState

      if (favoriteMangaList.isEmpty()) {
        IdleScreen(
          message = stringResource(R.string.you_haven_t_added_any_favorite_manga_yet),
          modifier = Modifier.fillMaxSize()
        )
      } else {
        val lazyGridState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()
        val isMoveToTopButtonVisible by remember(favoriteMangaList, lazyGridState) {
          derivedStateOf {
            favoriteMangaList.size > 15 && lazyGridState.firstVisibleItemIndex > 0
          }
        }

        Box(modifier = modifier) {
          LazyVerticalGrid(
            state = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
          ) {
            items(
              items = favoriteMangaList,
              key = { it.id }
            ) { manga ->
              FavoriteMangaItem(
                manga = manga,
                onSelectedManga = onSelectedManga,
                modifier = Modifier
                  .padding(4.dp)
                  .fillMaxWidth()
                  .height(250.dp)
              )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              ) {
                when (nextPageState) {
                  BaseNextPageState.LOADING -> NextPageLoadingIndicator(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(bottom = 12.dp)
                  )

                  BaseNextPageState.ERROR -> LoadPageErrorMessage(
                    message = stringResource(R.string.can_t_load_next_manga_page_please_try_again),
                    onRetry = onRetryObserveFavoriteMangaListNextPage,
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 8.dp)
                  )

                  BaseNextPageState.IDLE -> LoadMoreMessage(
                    onLoadMore = onObserveFavoriteMangaListNextPage,
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
              }
            }
          }

          AnimatedVisibility(
            visible = isMoveToTopButtonVisible,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(16.dp)
          ) {
            MoveToTopButton(
              onClick = {
                coroutineScope.launch {
                  lazyGridState.animateScrollToItem(0)
                }
              },
              modifier = Modifier.size(56.dp)
            )
          }
        }
      }
    }
  }
}
