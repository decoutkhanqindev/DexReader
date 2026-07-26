package com.decoutkhanqindev.dexreader.presentation.screens.favorites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaRatingChip
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaStatusBadge
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.MangaInfo
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun FavoriteMangaItem(
  manga: FavoriteMangaModel,
  onSelectedManga: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val onClick = remember(manga.id) { { onSelectedManga(manga.id) } }
  var isImageLoaded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .animateItemOnAppear()
      .onClick(shape = MaterialTheme.shapes.medium) { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = MaterialTheme.shapes.medium,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      MangaCoverArt(
        url = manga.coverUrl,
        title = manga.title,
        modifier = Modifier
          .fillMaxSize()
          .shimmerLoading(
            shape = MaterialTheme.shapes.medium,
            isEnable = !isImageLoaded
          ),
        onImageLoaded = { isImageLoaded = true }
      )

      // Modern Gradient Overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = MaterialTheme.colorScheme.scrim,
            topAlpha = 0f,
            topCenterAlpha = 0.1f,
            bottomCenterAlpha = 0.8f,
            bottomAlpha = 1f,
            startY = 350f,
          )
      )

      if (manga.rating.isNotEmpty()) {
        MangaRatingChip(
          rating = manga.rating,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(8.dp)
        )
      }

      MangaStatusBadge(
        status = manga.status,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp)
      )

      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
      ) {
        MangaInfo(
          title = manga.title,
          author = manga.author,
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        )
      }
    }
  }
}

@Preview
@Composable
private fun FavoriteMangaItemOnGoingPreview() {
  DexReaderTheme {
    FavoriteMangaItem(
      manga = FavoriteMangaModel(
        id = "1",
        title = "One Piece",
        coverUrl = "",
        author = "Eiichiro Oda",
        status = MangaStatusValue.ON_GOING,
        rating = "9.1",
        follows = "2.3M",
      ),
      onSelectedManga = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
    )
  }
}

@Preview
@Composable
private fun FavoriteMangaItemCompletedPreview() {
  DexReaderTheme {
    FavoriteMangaItem(
      manga = FavoriteMangaModel(
        id = "2",
        title = "Fullmetal Alchemist",
        coverUrl = "",
        author = "Hiromu Arakawa",
        status = MangaStatusValue.COMPLETED,
        rating = "9.2",
        follows = "1.1M",
      ),
      onSelectedManga = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
    )
  }
}
