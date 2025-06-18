package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import kotlinx.coroutines.launch

@Composable
fun VerticalGridMangaList(
  mangaList: List<Manga>,
  onSelectedManga: (Manga) -> Unit,
  loadMoreContent: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val lazyGridState = rememberLazyGridState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible = mangaList.size > 15 &&
      lazyGridState.firstVisibleItemIndex > 0

  Box(modifier = modifier) {
    LazyVerticalGrid(
      state = lazyGridState,
      modifier = Modifier.fillMaxSize(),
      columns = GridCells.Fixed(2),
      verticalArrangement = Arrangement.spacedBy(2.dp),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      itemsIndexed(
        items = mangaList,
        key = { index, manga -> "${manga.id}_$index" }
      ) { index, manga ->
        MangaItem(
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
        ) { loadMoreContent() }
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