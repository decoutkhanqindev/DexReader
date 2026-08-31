package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaRatingChip
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaStatusBadge
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaItem(
  item: MangaModel,
  modifier: Modifier = Modifier,
  onClick: (String) -> Unit,
) {
  val onClick = remember(item.id) { { onClick(item.id) } }
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
        url = item.coverUrl,
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
            alphas = persistentListOf(0f, 0.1f, 0.8f, 1f),
            color = MaterialTheme.colorScheme.scrim,
            startY = 350f,
          )
      )

      if (item.rating.isNotEmpty()) {
        MangaRatingChip(
          rating = item.rating,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(8.dp)
        )
      }

      MangaStatusBadge(
        status = item.status,
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
          title = item.title,
          author = item.author,
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
private fun MangaItemPreview() {
  DexReaderTheme {
    MangaItem(
      item = MangaModel(
        id = "manga-001",
        title = "One Piece",
        coverUrl = "",
        description = "Follow Monkey D. Luffy on his grand adventure to become King of the Pirates.",
        author = "Eiichiro Oda",
        artist = "Eiichiro Oda",
        categories = persistentListOf(),
        status = MangaStatusValue.ON_GOING,
        contentRating = MangaContentRatingValue.SAFE,
        year = "1997",
        availableLanguages = persistentListOf(),
        latestChapter = "1100",
        updatedAt = "2024-01-01",
        rating = "8.5",
        follows = "2.3M",
      ), modifier = Modifier
        .width(194.dp)
        .height(250.dp), onClick = {})
  }
}