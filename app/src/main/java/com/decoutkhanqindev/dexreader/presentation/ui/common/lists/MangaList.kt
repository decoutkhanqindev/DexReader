package com.decoutkhanqindev.dexreader.presentation.ui.common.lists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.MoveToTopButton
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HorizontalMangaList(
  mangaList: List<Manga>,
  onSelectedManga: (Manga) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(mangaList, key = { it.id }) { manga ->
        MangaItem(
          manga = manga,
          onSelectedManga = onSelectedManga,
          modifier = Modifier
            .padding(4.dp)
            .width(194.dp)
            .height(250.dp)
        )
      }
    }
  }
}

@Composable
fun VerticalGridMangaList(
  mangaList: List<Manga>,
  onSelectedManga: (Manga) -> Unit,
  loadMoreContent: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val lazyGridState = rememberLazyGridState()
  val coroutineScope = rememberCoroutineScope()
  val isMoveToTopButtonVisible = mangaList.size > 15 && lazyGridState.firstVisibleItemIndex > 0

  Box(modifier = modifier) {
    LazyVerticalGrid(
      state = lazyGridState,
      modifier = Modifier.fillMaxSize(),
      columns = GridCells.Fixed(2),
      verticalArrangement = Arrangement.spacedBy(2.dp),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      items(mangaList, key = { it.id }) { manga ->
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
        ) {
          loadMoreContent()
        }
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
            lazyGridState.animateScrollToItem(0)
          }
        },
        modifier = Modifier.size(56.dp)
      )
    }
  }
}

@Composable
private fun MangaItem(
  manga: Manga,
  onSelectedManga: (Manga) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    onClick = { onSelectedManga(manga) },
    elevation = CardDefaults.cardElevation(8.dp),
    shape = MaterialTheme.shapes.large,
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(manga.coverUrl)
          .crossfade(true)
          .memoryCachePolicy(CachePolicy.ENABLED)
          .diskCachePolicy(CachePolicy.ENABLED)
          .build(),
        contentDescription = manga.title,
        error = painterResource(R.drawable.placeholder),
        placeholder = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
      )
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        MangaInfo(
          manga = manga,
          modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(MaterialTheme.colorScheme.surface.copy(0.8f))
            .padding(horizontal = 4.dp)
        )
      }
    }
  }
}

@Composable
private fun MangaInfo(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = manga.title,
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.by_author, manga.author),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = manga.status.capitalize(Locale.US),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(bottom = 4.dp)
    )
  }
}
