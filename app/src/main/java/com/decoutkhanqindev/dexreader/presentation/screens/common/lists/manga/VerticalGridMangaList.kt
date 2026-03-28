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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun VerticalGridMangaList(
  mangaList: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onSelectedManga: (MangaModel) -> Unit,
  loadMoreContent: @Composable () -> Unit,
) {
  val lazyGridState = rememberLazyGridState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible by remember(mangaList, lazyGridState) {
    derivedStateOf {
      val lastVisibleIndex = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
      val totalItems = lazyGridState.layoutInfo.totalItemsCount
      lastVisibleIndex >= totalItems - 5
    }
  }

  Box(modifier = modifier) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier.fillMaxSize(),
      state = lazyGridState,
      verticalArrangement = Arrangement.spacedBy(2.dp),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      items(
        items = mangaList,
        key = MangaModel::id
      ) { manga ->
        MangaItem(
          item = manga,
          modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(250.dp),
        ) { onSelectedManga(it) }
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
      MoveToTopButton(modifier = Modifier.size(56.dp)) {
        coroutineScope.launch {
          lazyGridState.animateScrollToItem(0)
        }
      }
    }
  }
}