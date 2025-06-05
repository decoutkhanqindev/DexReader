package com.decoutkhanqindev.dexreader.presentation.ui.categories.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.categories.CategoriesUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.MoveToTopButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.ErrorScreen
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import kotlinx.coroutines.launch

@Composable
fun CategoriesContent(
  uiState: CategoriesUiState,
  onSelectedCategory: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  when (uiState) {
    CategoriesUiState.Loading -> LoadingScreen(modifier = modifier)

    CategoriesUiState.Error -> ErrorScreen(
      message = stringResource(R.string.oops_something_went_wrong_please_try_again),
      onRetry = onRetry,
      modifier = modifier
    )

    is CategoriesUiState.Success -> {
      val lazyListState = rememberLazyListState()
      val coroutineScope = rememberCoroutineScope()
      val isMoveToTopButtonVisible = lazyListState.firstVisibleItemScrollOffset > 0

      Box(modifier = modifier) {
        LazyColumn(
          state = lazyListState,
          modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 4.dp)
        ) {
          item {
            CategoryGroupSection(
              categoryList = uiState.genreList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              categoryList = uiState.themeList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              categoryList = uiState.formatList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }

          item {
            CategoryGroupSection(
              categoryList = uiState.contentList,
              onSelectedCategory = onSelectedCategory,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )
          }
        }

        AnimatedVisibility(
          visible = isMoveToTopButtonVisible,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
        ) {
          MoveToTopButton(
            onClick = {
              coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
              }
            },
            modifier = Modifier.size(56.dp)
          )
        }
      }
    }
  }
}